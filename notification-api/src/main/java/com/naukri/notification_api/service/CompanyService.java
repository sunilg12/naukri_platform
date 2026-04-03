package com.naukri.notification_api.service;

import com.naukri.notification_api.model.AppUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Service
public class CompanyService {

    public JavaMailSenderImpl getJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("sunilreddy200010@gmail.com");
        javaMailSender.setPassword("tylojctgjxigzdfg");

        Properties props = javaMailSender.getJavaMailProperties();
        //smtp.auht, true is used to authenticate the api to use email
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return javaMailSender;
    }

    public TemplateEngine getTemplateEngine(){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");// make sure this folder exists in resources
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public void sendInvitationMailToRecruiter(AppUser recruiter, String token) throws MessagingException {

        String acceptLink = "http://localhost:8081/api/central/company/accept-invitation/" + token;
        JavaMailSenderImpl javaMailSender = this.getJavaMailSender();
        // to send email we need to create email content with the help of MimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //we use MimeMessageHelper to set the subject content
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(recruiter.getEmail());
        String subjectLine = "Invitation to join ra puka " + recruiter.getCompany().getCompanyName();
        mimeMessageHelper.setSubject(subjectLine);

        Context context = new Context();
        context.setVariable("recruiterName", recruiter.getName());
        context.setVariable("companyName", recruiter.getCompany().getCompanyName());
        context.setVariable("acceptLink", acceptLink);

        TemplateEngine templateEngine = this.getTemplateEngine();
        //template file name and context created above
        String htmlContent = templateEngine.process("invite-recruiter", context);
        mimeMessageHelper.setText(htmlContent, true);

        //this sends mail for us
        javaMailSender.send(mimeMessage);

    }

    public void sendAcceptNotificationMailToAdmin(AppUser recruiter, AppUser admin) throws MessagingException {
        JavaMailSenderImpl javaMailSender = this.getJavaMailSender();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        Context context = new Context();
        context.setVariable("adminName", admin.getName());
        context.setVariable("recruiterName", recruiter.getName());
        context.setVariable("recruiterEmail", recruiter.getEmail());
        context.setVariable("recruiterPhone", recruiter.getPhoneNumber());
        mimeMessageHelper.setTo(admin.getEmail());
        mimeMessageHelper.setSubject("Invitation accepted by " + recruiter.getName());

        String htmlContent = this.getTemplateEngine().process("accept-invitation", context);
        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }
}
