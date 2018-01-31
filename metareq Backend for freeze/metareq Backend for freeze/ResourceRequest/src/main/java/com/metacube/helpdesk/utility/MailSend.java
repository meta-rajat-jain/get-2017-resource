package com.metacube.helpdesk.utility;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;


@Component  
public class MailSend{  
    private MailSender mailSender;  
   
    public void setMailSender(MailSender mailSender) {  
        this.mailSender = mailSender;  
    }  
   
    public void sendMail(String from, String to, String subject, String msg) {  
        //creating message  
        SimpleMailMessage message = new SimpleMailMessage();  
        message.setFrom(from);  
        message.setTo(to);  
        message.setSubject(subject);  
        message.setText(msg);  
        //sending message  
        mailSender.send(message);     
    }  
}  