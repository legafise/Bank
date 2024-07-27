package com.lashkevich.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

public class Transaction
        extends Entity {

    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime dateTime;

    public Transaction() {
    }

    public Transaction(long id, long fromAccountId, long toAccountId, BigDecimal amount,
                       Currency currency, LocalDateTime dateTime) {
        super(id);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.dateTime = dateTime;
    }


    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return fromAccountId == that.fromAccountId && toAccountId == that.toAccountId && Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromAccountId, toAccountId, amount, currency, dateTime);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", currency=" + currency +
                ", dateTime=" + dateTime +
                "} " + super.toString();
    }

}
