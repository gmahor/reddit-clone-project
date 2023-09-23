package com.reddit.services;

import com.reddit.configs.EnvConfiguration;
import com.reddit.constant.EmailConstants;
import com.reddit.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmailService {

    private final EnvConfiguration envConfiguration;

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public EmailService(EnvConfiguration envConfiguration,
                        TemplateEngine templateEngine,
                        JavaMailSender javaMailSender) {
        this.envConfiguration = envConfiguration;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendAccountVerifyEmail(String email,String token, String username) {
        log.info("Account verify link send to email : {}", email);
        Context context = new Context();
        context.setVariable(EmailConstants.USERNAME, username);
        context.setVariable("token", token);
        this.sendEmail(email, context, EmailConstants.SUBJECT_ACCOUNT_VERIFY, EmailConstants.TEMPLATE_ACCOUNT_VERIFY);
        log.info("Account verification mail send successfully : {}", email);
    }

    @Async
    public void sendCommentNotification(String email, String username,String postName) {
        log.info("Comment notification link send to email : {}", email);
        Context context = new Context();
        context.setVariable(EmailConstants.USERNAME, username);
        context.setVariable(EmailConstants.POST_NAME, postName);
        this.sendEmail(email, context, EmailConstants.SUBJECT_COMMENT_POST, EmailConstants.TEMPLATE_COMMENT_POST);
        log.info("Comment notification mail send successfully : {}", email);
    }

    private void sendEmail(String email, Context context, String emailSubject, String template) {
        CompletableFuture.runAsync(() -> {
            try {
                String emailBody = templateEngine.process(template, context);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);
                msg.setSubject(emailSubject);
                msg.setText(emailBody, true);
                msg.setTo(email);
                msg.setFrom(envConfiguration.getMailFromMailAddress());
                javaMailSender.send(mimeMessage);
                log.info("Mail sent successfully");
            } catch (MessagingException e) {
                log.info("Error in sending mail:: Subject- {}, EmailId- {}", emailSubject, email, e);
            }
        });
    }


}

