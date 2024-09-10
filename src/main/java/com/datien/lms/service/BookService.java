package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Book;
import com.datien.lms.dao.BookTransactionHistory;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.BookTransactionDto;
import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.dto.response.BookTransactionResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.repository.BookTransactionHistoryRepository;
import com.datien.lms.repository.UserRepository;
import com.datien.lms.service.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final Logger logger = LogManager.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileService fileService;

//    public void createBook(
//            BookRequest bookRequest,
//            Authentication connectedUser
//    ) {
//        User user = (User) connectedUser.getPrincipal();
//        if(user.getRole() != Role.ADMIN) {
//            throw new AccessDeniedException("You dont have permission to add book to the library");
//        }
//
//        var book = new Book();
//        book.setTitle(bookRequest.getTitle());
//        book.setAuthor(bookRequest.getAuthor());
//        book.setPublisher(bookRequest.getPublisher());
//        book.setISBN(bookRequest.getISBN());
//        book.setSynopsis(bookRequest.getSynopsis());
//        book.setPageCount(bookRequest.getPageCount());
//        book.setGenre(bookRequest.getGenre());
//        book.setAvailable(bookRequest.isAvailable());
//        book.setArchived(bookRequest.isArchived());
//        book.setCreatedBy(user.getId());
//        book.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
//        book.setLastModifiedBy(user.getUsername());
//        bookRepository.save(book);
//
//    }

    public Map<Object, Object> createBook(BookRequest bookRequest, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result;

        try {
            var user1 = userRepository.findByEmail(connectedUser.getName());

            if(user1.isEmpty()) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            if(!user1.get().isEnabled()) {
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

//            User user = (User) connectedUser.getPrincipal(); // Khai báo biến user ở đây
            if (user1.get().getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var book = new Book();
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPublisher(bookRequest.getPublisher());
            book.setISBN(bookRequest.getISBN());
            book.setSynopsis(bookRequest.getSynopsis());
            book.setPageCount(bookRequest.getPageCount());
            book.setGenre(bookRequest.getGenre());
            book.setAvailable(bookRequest.isAvailable());
            book.setArchived(bookRequest.isArchived());
            book.setCreatedBy(user1.get().getId());
            book.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            book.setLastModifiedBy(user1.get().getUsername());

            bookRepository.save(book);

            result = new Result(ResponseCode.SYSTEM.getCode(), true, ResponseCode.SYSTEM.getMessage());
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
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
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> getDetailBook(Long bookId) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            var book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with Id: " + bookId));
            var savedBook = bookMapper.toBookResponse(book);

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, savedBook);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;

    }

//    public BookResponse getDetailBook(Long bookId) {
//        var book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));
//        return bookMapper.toBookResponse(book);
//    }

    public Map<Object, Object> deleteBook(Long bookId, boolean hardDelete, Authentication connectedUser) {
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
            if(!hardDelete) {
                book.setArchived(true);
            } else {
                bookRepository.deleteById(bookId);

            }
            notification = "Successfully delete book.";
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }


    public Map<Object, Object> borrowBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            if (!book.isAvailable()) {
                result = new Result(ResponseCode.BOOK_NOT_AVAILABLE.getCode(), false, ResponseCode.BOOK_NOT_AVAILABLE.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            book.setAvailable(false);
            bookRepository.save(book);
            notification = "Successfully borrowed book with the Id " + bookId;

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, book.getId().intValue());
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> returnBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            book.setAvailable(true);
            bookRepository.save(book);

        } catch (Exception ex) {
            logger.error("Something errors occur!");
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> returnApproveBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecute = new HashMap<>();
        Result result = new Result();

        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            User user = (User) connectedUser.getPrincipal();

            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            book.setAvailable(true);
            bookRepository.save(book);

        } catch (Exception ex) {
            logger.error("Some errors occurs in returnApproveBook", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecute;
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

        BookTransactionResponse bookTransactionResponse = new BookTransactionResponse();
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

            bookTransactionResponse.setBookTransactionDtos(paginatedList);
            bookTransactionResponse.setTotalRecords(bookTransactionDtos.size());

        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecute;
    }

    public Map<Object, Object> uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Long bookId) {
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

            var profilePicture = fileService.saveFile(file, bookId, user.getId());
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
