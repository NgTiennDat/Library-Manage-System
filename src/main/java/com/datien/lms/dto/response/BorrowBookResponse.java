package com.datien.lms.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBookResponse {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String synopsis;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
