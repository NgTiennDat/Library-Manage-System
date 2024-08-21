package com.datien.lms.service;

import com.datien.lms.dao.Otp;
import com.datien.lms.dao.User;
import com.datien.lms.repository.OtpRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final OtpRepository otpRepository;

    @Async
    public void sendEmail(
            String to,
            String username,
            String subject,
            String activationCode
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("activationCode", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("ntdat14092003@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("mail to activate the account. The active code is " + activationCode);

        mailSender.send(message);
        System.out.println("Email sent successfully");
    }

    @Async
    public void sendValidEmail(User user) throws MessagingException {
        String activeCode = generateAndSavedActiveCode(user);
        this.sendEmail(
                user.getEmail(),
                user.getUsername(),
                "Account activation",
                activeCode
        );
    }

    private String generateAndSavedActiveCode(User user) {
        String activateCode = generateActiveCode(6);

        var otp = Otp.builder()
                .code(activateCode)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        otpRepository.save(otp);
        return activateCode;
    }

    private String generateActiveCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
