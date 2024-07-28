package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;

public class HelpCommand
        implements Command {

    private static final String SEPARATOR = "=====================================";

    @Override
    public void execute(String[] params) {
        System.out.println("Available commands:");

        System.out.println("help - Show this help message");
        System.out.println("stop - Stop this application");

        System.out.println(SEPARATOR);

        System.out.println("createBank name individualFee legalEntityFee - Add a new bank");
        System.out.println("findBankById id - Find bank");
        System.out.println("addClient clientId bankId currency - Add client to bank and open account");

        System.out.println(SEPARATOR);

        System.out.println("createClient name type - Create new client");
        System.out.println("findClientById id - Find client");

        System.out.println(SEPARATOR);

        System.out.println("findTransactionById id - Find transaction");
        System.out.println("transferCash accountFromId accountToId amount - Transfer cash between accounts");

        System.out.println(SEPARATOR);

        System.out.println("findAccountById accountId - Find account");
        System.out.println("topUpBalance accountId amount - Top up account balance");
    }

}
