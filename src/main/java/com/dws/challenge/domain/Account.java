package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;
  private String accountFromId;
  private String accountToId;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  @JsonCreator
  public Account(@JsonProperty("accountFromId") String accountFromId,
                 @JsonProperty("accountToId") String accountToId,
                 @JsonProperty("balance") BigDecimal balance) {
    this.accountFromId = accountFromId;
    this.accountToId = accountToId;
    this.balance = balance;
    accountId = null;
  }
}
