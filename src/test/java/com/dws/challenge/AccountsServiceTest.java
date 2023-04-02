package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  void addAccount() {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  void addAccount_failsOnDuplicateId() {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }

  @Test
  void testTransferAmount() throws Exception {
    Account account1 = new Account("ID-234", BigDecimal.valueOf(1000));
    Account account2 = new Account("ID-235", BigDecimal.valueOf(500));
    accountsService.createAccount(account1);
    accountsService.createAccount(account2);

    accountsService.transferAccount(account1.getAccountFromId(), account2.getAccountToId(),BigDecimal.valueOf(300));

    // Verify: check that the balances are updated correctly
    Account updatedAccount1 = accountsService.getAccount(account1.getAccountFromId());
    Account updatedAccount2 = accountsService.getAccount(account2.getAccountToId());
    assertEquals(String.valueOf(700), updatedAccount1.getBalance(), 0.0);
    assertEquals(String.valueOf(800), updatedAccount2.getBalance(), 0.0);
  }
}
