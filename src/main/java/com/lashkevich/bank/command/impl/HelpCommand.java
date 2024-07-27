package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;

public class HelpCommand
        implements Command {

    @Override
    public void execute(String[] params) {
        System.out.println("Available commands:");
        System.out.println("help - Show this help message");
        System.out.println("add_bank name individualFee legalEntityFee - Add a new bank");
        System.out.println("stop - Stop this application");
    }

}
