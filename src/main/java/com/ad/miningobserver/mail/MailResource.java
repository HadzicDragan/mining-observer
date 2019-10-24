package com.ad.miningobserver.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = {"/email"},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE}
)
public class MailResource {
    
    @Autowired
    private MailService serviceTest;
    
    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public ResponseEntity getAccountOwner() {
        this.serviceTest.sendMail();
        
        return ResponseEntity.ok().build();
    }
}
