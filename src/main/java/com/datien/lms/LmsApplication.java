package com.datien.lms;

import com.datien.lms.dao.SEX;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static com.datien.lms.dao.Role.ADMIN;
import static com.datien.lms.dao.Role.MANAGER;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }
//
//    @Bean
//    public CommandLineRunner runner(
//            UserService service
//    ) {
//        return args -> {
//            var admin = UserRequest.builder()
//                    .firstname("Admin")
//                    .lastname("Admin")
//                    .email("admin@mail.com")
//                    .password("password1")
//                    .role(ADMIN)
//                    .loginCount(0)
//                    .sex(SEX.MALE)
//                    .phone("12429238231")
//                    .enabled(true)
//                    .build();
//            service.register(admin);
//            var manager = UserRequest.builder()
//                    .firstname("Manager")
//                    .lastname("Manager")
//                    .email("manager@mail.com")
//                    .password("password2")
//                    .role(MANAGER)
//                    .loginCount(0)
//                    .sex(SEX.FEMALE)
//                    .enabled(true)
//                    .phone("12429238231")
//                    .build();
//            service.register(manager);
//        };
//    }
}
