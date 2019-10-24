package com.ad.miningobserver.mail.boundary;

import com.ad.miningobserver.mail.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    private final AccountClient client;
    private AccountHolder accountRef;
    
    @Value("${default.email.account}")
    private String defaultEmail;
    
    @Autowired
    public AccountService(AccountClient client) {
        this.client = client;
    }
    
    public Account getAccountOwner() {
        if (this.accountRef == null) {
            this.accountRefOrDefault();
        }
        return this.getAccount();
    }
    
    public void setAccountReference() {
        final Account newAccount = this.client.getAdministratorDetails();
        if (this.isAccountNullOrEmail(newAccount)) {
            return;
        }
        if (!this.getAccount().equals(newAccount)) {
            this.accountRef = new AccountHolder(newAccount);
        }
    }
    
    private void accountRefOrDefault() {
        final Account account = this.client.getAdministratorDetails();
        if (this.isAccountNullOrEmail(account)) {
            this.setDefaultAccount();
        } else {
            this.accountRef = new AccountHolder(account);
        }
    }

    private boolean isAccountNullOrEmail(final Account account) {
        return (account == null) || (account.getEmail() == null);
    }
    
    private Account getAccount() {
        return this.accountRef.getAccountRef();
    }
    
    private void setDefaultAccount() {
        final Account account = new Account();
        account.setEmail(this.defaultEmail);
        this.accountRef = new AccountHolder(account);
    }
    
    private final class AccountHolder {
        private final Account accountRef;

        public AccountHolder(Account accountRef) {
            this.accountRef = accountRef;
        }

        public Account getAccountRef() {
            return this.accountRef;
        }
    }
}
