package com.datien.lms.dto.response;

import com.datien.lms.dto.BookTransactionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookTransactionResponse {
    private List<BookTransactionDto> bookTransactionDtos;
    private int totalRecords;

    public void setTotalPages(int totalPages) {
    }
}
