package com.datien.lms.role;

import com.datien.lms.user.dao.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_role")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    private String name;

    @OneToOne
    private User user;

}
