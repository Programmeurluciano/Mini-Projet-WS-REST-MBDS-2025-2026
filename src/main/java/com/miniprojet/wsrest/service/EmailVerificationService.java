package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.model.User;
import com.miniprojet.wsrest.model.VerificationToken;
import com.miniprojet.wsrest.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final VerificationTokenRepository tokenRepository;

    public EmailVerificationService(EmailService emailService, EmailTemplateService emailTemplateService, VerificationTokenRepository tokenRepository) {
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
        this.tokenRepository = tokenRepository;
    }

    public String generateVerificationToken(User user) {

        String tokenStr = UUID.randomUUID().toString();

        VerificationToken token = new VerificationToken();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusSeconds(60 * 60 * 24));
        tokenRepository.save(token);

        String link = "http://localhost:8080/api/auth/confirm?token=" + tokenStr;
        String html = emailTemplateService.buildConfirmationEmail(user.getFullname(), link);

        emailService.sendHtmlEmail(user.getEmail(),"Confirm your account", html);

        return tokenStr;
    }

    public User verifyToken(String tokenStr) {

        VerificationToken token = tokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Token invalid or expired"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = token.getUser();
        user.setEnabled(true);
        tokenRepository.delete(token);
        return user;
    }

}
