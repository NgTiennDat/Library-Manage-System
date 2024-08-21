package com.datien.lms.dao;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbl_manager")
public class Manager extends User {
}
