package com.datien.lms.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBookResponse {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String synopsis;
    private boolean returned;
    private boolean returnApproved;
}
