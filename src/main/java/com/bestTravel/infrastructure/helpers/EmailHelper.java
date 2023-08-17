package com.bestTravel.infrastructure.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailHelper {
    private final JavaMailSender mailSender;

    private final Path TEMPLATE_PATH = Paths.get("src/main/resources/email/email_template.html");

    public void sendMail(String to, String name, String product){
        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = this.readHtmlTemplate(name, product);
        try {
            message.setFrom(new InternetAddress("caitocd@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        }catch (MessagingException e){
            log.error("error to send mail: {}", e.getMessage());
        }
    }

    private String readHtmlTemplate(String name, String product){

        try(var lines = Files.lines(TEMPLATE_PATH)) {
            var html = lines.collect(Collectors.joining());
            return html.replace("{name}", name).replace("{product}", product);
        }catch (IOException e){
            log.error("error al leer html: {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}
