package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.common.Utility;
import com.datien.lms.dao.Book;
import com.datien.lms.dao.BookTransactionHistory;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.BookTransactionDto;
import com.datien.lms.dto.request.model.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.dto.response.BookTransactionResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.repository.BookTransactionHistoryRepository;
import com.datien.lms.repository.UserRepository;
import com.datien.lms.service.mapper.BookMapper;
import com.datien.lms.utils.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final String INV_CHECKSUM_KEY = "BOOK";
    private final Logger logger = LogManager.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileService fileService;
    private final RedisService redisService;

    public Map<Object, Object> createBook(BookRequest bookRequest, MultipartFile bookCover, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var user = userRepository.findByEmailAndIsDeleted(connectedUser.getName(), AppConstant.STATUS.IS_UN_DELETED);

            if(user == null) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            if(!user.isEnabled()) {
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

//            User user = (User) connectedUser.getPrincipal(); // Khai báo biến user ở đây
            if (user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            String bookId = UUID.randomUUID().toString();
            var book = new Book();
            book.setId(bookId);
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPublisher(bookRequest.getPublisher());
            book.setISBN(bookRequest.getISBN());
            book.setSynopsis(bookRequest.getSynopsis());
            book.setPageCount(bookRequest.getPageCount());
            book.setGenre(bookRequest.getGenre());
            book.setAvailable(bookRequest.isAvailable());
            book.setArchived(bookRequest.isArchived());
            book.setCreatedBy(user.getId());
            book.setCreatedAt(LocalDateTime.now());
            book.setLastModifiedBy(user.getUsername());

            String coverPath = fileService.saveFile(bookCover, user.getId());
            if(coverPath == null) {
                result = new Result(ResponseCode.PATH_NOT_FOUND.getCode(), false, ResponseCode.PATH_NOT_FOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }
            book.setBookCover(coverPath);

            bookRepository.save(book);
            notification = "Successfully added book.";

        } catch (Exception ex) {
            logger.error("Some errors occurs when adding a book.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Add book failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> getAllBook(int page, int size) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
            Page<Book> books = bookRepository.findAll(pageable);
            List<BookResponse> bookResponses = books
                    .stream()
                    .map(bookMapper::toBookResponse)
                    .collect(Collectors.toList());
            Page<BookResponse> bookResponsesPage = new PageImpl<>(bookResponses, pageable, books.getTotalElements());

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, bookResponsesPage);
        } catch (Exception ex) {
            logger.error("Some errors occurs when retrieving all books.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> getDetailBook(String bookId) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            var book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));
            var savedBook = bookMapper.toBookResponse(book);

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, savedBook);
        } catch (Exception ex) {
            logger.error("Some errors occurs when retrieving book.", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;

    }

    public Map<Object, Object> deleteBook(String bookId, String isDeleted, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();

            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ROLE_NOT_VALID.getCode(), false, ResponseCode.ROLE_NOT_VALID.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            var book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));

            book.setIsDeleted(isDeleted);
            bookRepository.save(book);

            notification = "Successfully delete book.";
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> findAllBookByISBN(
            String isbn, int page, int size, Authentication connectedUser
    ) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();

            if(user.getRole() != Role.ADMIN) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<Book> books = bookRepository.findByISBN(isbn, pageable);
            List<BookResponse> bookResponses = books.stream()
                    .map(bookMapper::toBookResponse)
                    .toList();

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, bookResponses);

        } catch (Exception ex) {
            logger.error("Some errors occurs in findAllBookByISBN", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> getAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        BookTransactionResponse bookTransactionResponse = new BookTransactionResponse();
        List<BookTransactionDto> bookTransactionDtos = new ArrayList<>();
        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            // Fetch the borrowed books based on pagination
            Pageable pageable = PageRequest.of(page, size);
            Page<BookTransactionHistory> bookTransactions = bookTransactionHistoryRepository.findAllByStatus(pageable);

            // Convert to DTOs
            bookTransactionDtos = bookTransactions.stream()
                    .map(bookTransaction -> {
                        BookTransactionDto bookTransactionDto = new BookTransactionDto();
                        bookTransactionDto.setBook(bookTransaction.getBook());
                        bookTransactionDto.setUserId(bookTransaction.getUserId());
                        return bookTransactionDto;
                    }).collect(Collectors.toList());

            bookTransactionResponse.setBookTransactionDtos(bookTransactionDtos);
            bookTransactionResponse.setTotalRecords((int) bookTransactions.getTotalElements());
            bookTransactionResponse.setTotalPages(bookTransactions.getTotalPages());

//            result = new Result(ResponseCode.SUCCESS.getCode(), true, ResponseCode.SUCCESS.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, bookTransactionResponse);

        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }


    public Map<Object, Object> getAllReturnedBooks(int page, int size, Authentication connectedUser) {
        Map<Object, Object> resultExecute = new HashMap<>();
        Result result = new Result();

        BookTransactionResponse response = new BookTransactionResponse();
        List<BookTransactionDto> bookTransactionDtos = new ArrayList<>();
        try {
            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecute;
            }

            List<BookTransactionHistory> bookTransactions = bookTransactionHistoryRepository.findAllByReturned();

            bookTransactionDtos = bookTransactions.stream()
                    .map(bookTransaction -> {
                        BookTransactionDto bookTransactionDto = new BookTransactionDto();
                        bookTransactionDto.setBook(bookTransaction.getBook());
                        bookTransactionDto.setUserId(bookTransaction.getUserId());
                        bookTransactionDto.setReturned(bookTransaction.isReturned());
                        bookTransactionDto.setReturnApproved(bookTransaction.isReturnApproved());
                        return bookTransactionDto;
                    }).toList();

            List<BookTransactionDto> paginatedList = bookTransactionDtos.stream()
                    .skip((long) page * size)
                    .limit(size)
                    .toList();

            response.setBookTransactionDtos(paginatedList);
            response.setTotalRecords(bookTransactionDtos.size());

        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecute;
    }

    public Map<Object, Object> uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, String bookId) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            var book = bookRepository.findByIdAndIsDeleted(bookId);
            if(book == null) {
                result = new Result(ResponseCode.BOOK_NOT_FOUND.getCode(), false, ResponseCode.BOOK_NOT_FOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var profilePicture = fileService.saveFile(file, user.getId());
            book.setBookCover(profilePicture);
            bookRepository.save(book);

        } catch (Exception ex) {
            logger.error("Some errors occurs in uploadBookCoverPicture", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }
}
