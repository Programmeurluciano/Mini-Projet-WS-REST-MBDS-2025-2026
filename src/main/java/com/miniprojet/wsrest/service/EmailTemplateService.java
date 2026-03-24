package com.miniprojet.wsrest.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service @RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String buildConfirmationEmail(String name, String confirmationLink) {

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("confirmationLink", confirmationLink);

        return templateEngine.process("email/confirm-account", context);
    }

}
