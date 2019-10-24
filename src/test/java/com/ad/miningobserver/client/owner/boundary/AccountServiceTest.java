package com.ad.miningobserver.client.owner.boundary;

import com.ad.miningobserver.mail.boundary.AccountService;
import com.ad.miningobserver.mail.boundary.AccountClient;
import com.ad.miningobserver.mail.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AccountServiceTest {
    
    @Autowired
    private AccountService service;
    
    @Value("${default.email.account}")
    private String defaultEmail;
    
    @MockBean
    private AccountClient client;
    
    @Test
    public void a_getAccountOwner_nullEmailAccount() {
        Account mockedAccount = Mockito.mock(Account.class);
        Mockito.when(this.client.getAdministratorDetails()).thenReturn(mockedAccount);
        
        final Account accountOwner = this.service.getAccountOwner();
        Assertions.assertThat(accountOwner.getEmail())
                .isNotNull();
        Assertions.assertThat(accountOwner.getEmail())
                .isEqualTo(this.defaultEmail);
    }
    
    @Test
    public void b_getAccountOwner_nullReference_defaultEmail() {
        Mockito.when(this.client.getAdministratorDetails()).thenReturn(null);
        final Account account = this.service.getAccountOwner();
        Assertions.assertThat(account)
                .isNotNull();
        Assertions.assertThat(account.getEmail())
                .isEqualTo(this.defaultEmail);
    }
    
    @Test
    public void c_getAccountOwner_setReference() {
        final String accountEmail = "new.test.user@gmail.com";
        Account mockedAccount = Mockito.mock(Account.class);
        Mockito.when(mockedAccount.getEmail()).thenReturn(accountEmail);
        Mockito.when(this.client.getAdministratorDetails()).thenReturn(mockedAccount);
        
        this.service.setAccountReference();
        
        final Account account = this.service.getAccountOwner();
        Assertions.assertThat(account)
                .isNotNull();
        Assertions.assertThat(account.getEmail())
                .isEqualTo(accountEmail);
    }
    
    @Test
    public void d_setAccountReference_nullClientResponse() {
        final Account originalAccount = this.service.getAccountOwner();
        Mockito.when(this.client.getAdministratorDetails()).thenReturn(null);
        
        this.service.setAccountReference();
        final Account accountOwner = this.service.getAccountOwner();
        Assertions.assertThat(accountOwner)
                .isEqualTo(originalAccount);
    }
}
