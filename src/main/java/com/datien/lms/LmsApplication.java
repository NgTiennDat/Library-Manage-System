package com.datien.lms;

import com.datien.lms.dao.Role;
import com.datien.lms.dto.request.UserRequest;
import com.datien.lms.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

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
            UserRequest admin = new UserRequest();
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin123");
            admin.setRole(Role.ADMIN);
//            System.out.println("Admin token: " + service.register(admin).getAccessToken());
        };
    }

}
