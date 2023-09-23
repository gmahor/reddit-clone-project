package com.reddit.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;


@Configuration
public class EmailConfiguration {

    private final EnvConfiguration envConfiguration;

    @Autowired
    public EmailConfiguration(EnvConfiguration envConfiguration){
        this.envConfiguration=envConfiguration;
    }

    @Bean
    public JavaMailSender getJavaMailSender(){
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(envConfiguration.getMailServerHost());
        mailSender.setPort(envConfiguration.getMailServerPort());
        mailSender.setProtocol(envConfiguration.getMailServerProtocol());
        mailSender.setUsername(envConfiguration.getMailServerUsername());
        mailSender.setPassword(envConfiguration.getMailServerPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", envConfiguration.isMailServerAuthActive());
        props.put("mail.smtp.starttls.enable", envConfiguration.isMailServerTlsActive());
        props.put("mail.debug", envConfiguration.isMailDebugActive());

        return mailSender;
    }

    @Bean
    public TemplateEngine emailTemplateEngine(){
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(this.htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(this.emailMessageSource());
        return templateEngine;
    }


    @Bean
    public ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(envConfiguration.getMailTemplatesPath() + "/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }


    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }

}
