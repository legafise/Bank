package com.lashkevich.bank.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Bank
        extends Entity {

    private String name;
    private BigDecimal individualFee;
    private BigDecimal legalEntityFee;
    private List<Account> accounts;
    private List<Client> clients;

    public Bank() {
    }

    public Bank(long id, String name, BigDecimal individualFee, BigDecimal legalEntityFee, List<Account> accounts, List<Client> clients) {
        super(id);
        this.name = name;
        this.individualFee = individualFee;
        this.legalEntityFee = legalEntityFee;
        this.accounts = accounts;
        this.clients = clients;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIndividualFee() {
        return individualFee;
    }

    public void setIndividualFee(BigDecimal individualFee) {
        this.individualFee = individualFee;
    }

    public BigDecimal getLegalEntityFee() {
        return legalEntityFee;
    }

    public void setLegalEntityFee(BigDecimal legalEntityFee) {
        this.legalEntityFee = legalEntityFee;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bank bank = (Bank) o;
        return Objects.equals(name, bank.name) && Objects.equals(individualFee, bank.individualFee) && Objects.equals(legalEntityFee, bank.legalEntityFee) && Objects.equals(accounts, bank.accounts) && Objects.equals(clients, bank.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, individualFee, legalEntityFee, accounts, clients);
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", individualFee=" + individualFee +
                ", legalEntityFee=" + legalEntityFee +
                ", accounts=" + accounts +
                ", clients=" + clients +
                "} " + super.toString();
    }

}
