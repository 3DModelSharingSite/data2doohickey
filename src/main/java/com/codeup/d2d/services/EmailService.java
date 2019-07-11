//package com.codeup.d2d.services;
//
//import com.codeup.d2d.doohickeys.Post;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service("mailService")
//public class EmailService {
//
//    @Autowired
//    public JavaMailSender emailSender;
//
//    @Value("${spring.mail.from}")
//    private String from;
//
//    public void prepareAndSend(Post post) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
//        msg.setTo(post.getOwner().getEmail());
//        msg.setSubject(post.getTitle());
//        msg.setText(post.getBody());
//
//        try{
//            this.emailSender.send(msg);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }
//}