package com.ad.miningobserver.mail.boundary;

import com.ad.miningobserver.mail.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = {"/account/test"},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE}
)
public class AccountResource {
    
    @Autowired
    private AccountService service;
    
    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public ResponseEntity getContainerAccountEmail() {
        final Account accountOwner = this.service.getAccountOwner();
        return ResponseEntity.ok().body(accountOwner);
    }
}
