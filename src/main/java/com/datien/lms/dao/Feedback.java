package com.datien.lms.dao;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Feedback {
    private Double note;
    private String description;

    @ManyToOne
    @JoinColumn(name = "book-id")
    private Book book;
}
