package com.datien.lms.dto;

import com.datien.lms.dao.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookTransactionDto {
    private String userId;
    private Book book;
    private boolean returnApproved;
    private boolean returned;
}
