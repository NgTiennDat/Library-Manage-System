package com.datien.lms.dto.response;

import com.datien.lms.dto.BookDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookResponse {
    private List<BookDto> books;
    private int totalRecords;
}
