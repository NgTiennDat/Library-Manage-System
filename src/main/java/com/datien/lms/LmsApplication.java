package com.datien.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(UserService service) {
//        return args -> {
//            // Create Admin User
//            var admin = UserRequest.builder()
//                    .firstname("Admin")
//                    .lastname("Admin")
//                    .email("admin@mail.com")
//                    .password("password1")
//                    .role(ADMIN)
//                    .loginCount(0)
//                    .sex(SEX.MALE)
//                    .isDeleted("N")
//                    .status(0)
//                    .phone("12429238231")
//                    .enabled(true)
//                    .build();
//            service.register(admin);
//
//            // Create Manager User
//            var manager = UserRequest.builder()
//                    .firstname("Manager")
//                    .lastname("Manager")
//                    .email("manager@mail.com")
//                    .password("password2")
//                    .role(MANAGER)
//                    .loginCount(0)
//                    .isDeleted("N")
//                    .sex(SEX.FEMALE)
//                    .status(0)
//                    .enabled(true)
//                    .phone("12429238231")
//                    .build();
//            service.register(manager);
//        };
//    }

}
