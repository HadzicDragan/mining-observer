package com.ad.miningobserver.mail.boundary;

import com.ad.miningobserver.client.AbstractClient;
import com.ad.miningobserver.mail.control.AccountPath;
import com.ad.miningobserver.mail.entity.Account;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountClient extends AbstractClient {
    
    public Account getAdministratorDetails() {
        final String healthEndpoint = new AccountPath(this.containerPath)
                .buildOwnerPath();
        final ResponseEntity<Account> response = this.restTemplate.exchange(
                healthEndpoint, HttpMethod.GET, null, Account.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return response.getBody();
    }
}
