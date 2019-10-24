package com.ad.miningobserver.mail;

import com.ad.miningobserver.mail.boundary.AccountService;
import com.ad.miningobserver.mail.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    
    private AccountService accountService;
    
    private String getAccountEmail() {
        final Account account = this.accountService.getAccountOwner();
        return account.getEmail();
    }
    
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
