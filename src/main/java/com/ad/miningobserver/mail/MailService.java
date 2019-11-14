package com.ad.miningobserver.mail;

import com.ad.miningobserver.mail.boundary.AccountService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

// import org.apache.velocity.Template;
// import org.apache.velocity.VelocityContext;
// import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private JavaMailSender javaMailSender;

    // @Autowired
    // private VelocityEngine velEngine;
    
    public void sendMail() {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        
        final String text = MailComposer.buildEmailContent();
        final String accountEmail = this.accountService.getAccountOwner().getEmail();
        System.out.println(this.getClass().getSimpleName() + ".sendMail(): email -- " + accountEmail);
        
        try {
            messageHelper.setTo(accountEmail);
            messageHelper.setSubject("GPU error");
            messageHelper.setText(text, true);
        } catch (MessagingException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Template template = velEngine.getTemplate("criticalTemp.vm");

        // VelocityEngineUtils.

        // VelocityContext velContext = new VelocityContext();
        // velContext.
        
        System.out.println(text);
//        javaMailSender.send(mimeMessage);
    }
}
