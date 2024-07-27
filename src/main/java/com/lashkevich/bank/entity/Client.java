package com.lashkevich.bank.entity;

import java.util.List;

public class Client
        extends Entity {

    private String name;
    private ClientType type;
    private List<Account> accounts;

    public Client() {
    }

    public Client(long id, String name, ClientType type, List<Account> accounts) {
        super(id);
        this.name = name;
        this.type = type;
        this.accounts = accounts;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", accounts=" + accounts +
                "} " + super.toString();
    }

}
