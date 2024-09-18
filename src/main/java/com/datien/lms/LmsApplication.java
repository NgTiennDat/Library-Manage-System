package com.datien.lms;

import com.datien.lms.dao.SEX;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.UUID;

import static com.datien.lms.dao.Role.ADMIN;
import static com.datien.lms.dao.Role.MANAGER;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(
            UserService service
    ) {
        return args -> {
            var admin = new UserRequest();
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword("password1");
            admin.setRole(ADMIN);
            admin.setLoginCount(0);
            admin.setSex(SEX.MALE);
            admin.setIsDeleted("N");
            admin.setPhone("12429238231");
            admin.setEnabled(true);

            service.register(admin);

            var manager = new UserRequest();
            manager.setFirstname("Manager");
            manager.setLastname("Manager");
            manager.setEmail("manager@mail.com");
            manager.setPassword("password2");
            manager.setRole(MANAGER);
            manager.setLoginCount(0);
            manager.setIsDeleted("N");
            manager.setSex(SEX.FEMALE);
            manager.setPhone("12429238231");
            manager.setEnabled(true);

            service.register(manager);
        };
    }

}
