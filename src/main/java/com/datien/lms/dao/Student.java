package com.datien.lms.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbl_student")
public class Student extends User {
    private boolean isBorrowing;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

}
