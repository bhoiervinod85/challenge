package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired(required=false)
  private NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public synchronized void transferAccount(String accountFromId, String accountToId, BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }

    Account accountFrom = accountsRepository.getAccount(accountFromId);
            if(accountFrom == null){
              throw new IllegalArgumentException("Account from not found");
            }

    Account accountTo = accountsRepository.getAccount(accountToId);
            if(accountToId == null){
              throw new IllegalArgumentException("Account to not found");
            }

    if (accountFrom.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient balance");
    }

    accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
    accountTo.setBalance(accountTo.getBalance().add(amount));

    accountsRepository.TransferAmount(accountFrom);
    accountsRepository.TransferAmount(accountTo);

    notificationService.notifyAboutTransfer(accountFrom, "Transfer from account " + accountFrom.getAccountFromId() + ": " + accountFrom.getBalance());
    notificationService.notifyAboutTransfer(accountTo, "Transfer to account " + accountTo.getAccountToId() + ": " + accountTo.getBalance());
  }
}
