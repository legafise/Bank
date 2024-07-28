package com.lashkevich.bank.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Account
        extends Entity {

    private long bankId;
    private long clientId;
    private Currency currency;
    private List<Transaction> transactions;
    private BigDecimal balance;

    public Account() {
    }

    public Account(long id, long bankId, long clientId, Currency currency, List<Transaction> transactions, BigDecimal balance) {
        super(id);
        this.bankId = bankId;
        this.clientId = clientId;
        this.currency = currency;
        this.transactions = transactions;
        this.balance = balance;
    }


    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return bankId == account.bankId && clientId == account.clientId && Objects.equals(currency, account.currency) && Objects.equals(transactions, account.transactions) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bankId, clientId, currency, transactions, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "bankId=" + bankId +
                ", clientId=" + clientId +
                ", currency=" + currency +
                ", transactions=" + transactions +
                ", balance=" + balance +
                "} " + super.toString();
    }

}
