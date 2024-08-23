package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Book;
import com.datien.lms.dao.BookTransactionHistory;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.dto.response.BorrowBookResponse;
import com.datien.lms.exception.OperationNotPermittedException;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.service.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

//    public void createBook(
//            BookRequest bookRequest,
//            Authentication connectedUser
//    ) {
//        User user = (User) connectedUser;
//        if(user.getRole() != Role.ADMIN) {
//            throw new AccessDeniedException("You dont have permission to add book to the library");
//        }
//
//        var book = new Book();
//        book.setTitle(bookRequest.getTitle());
//        book.setAuthor(bookRequest.getAuthor());
//        book.setPublisher(bookRequest.getPublisher());
//        book.setSynopsis(bookRequest.getSynopsis());
//        book.setGenre(bookRequest.getGenre());
//        book.setAvailable(true);
//        bookRepository.save(book);
//    }

    public Map<Object, Object> createBook(BookRequest bookRequest, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

        try {
            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() != Role.ADMIN) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            var book = new Book();
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPublisher(bookRequest.getPublisher());
            book.setISBN(bookRequest.getISBN());
            book.setSynopsis(bookRequest.getSynopsis());
            book.setPageCount(bookRequest.getPageCount());
            book.setGenre(bookRequest.getGenre());
            book.setAvailable(true);

            bookRepository.save(book);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
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

    public Map<Object, Object> getAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        try {

            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("borrowDate").descending());
            Page<Book> borrowedBooks = bookRepository.findAllByIsAvailableFalse(pageable);
            List<BorrowBookResponse> borrowBookResponses = borrowedBooks
                    .stream()
                    .map(bookMapper::toBorrowBookResponse1)
                    .toList();
            Page<BorrowBookResponse> responsePage = new PageImpl<>(borrowBookResponses, pageable, borrowedBooks.getTotalElements());
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, responsePage);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> borrowBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
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
//            List<BookResponse> bookResponses = books.stream()
//                    .map(book -> bookMapper.toBorrowBookResponse(isbn, pageable, user.getId()))


        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }
}
