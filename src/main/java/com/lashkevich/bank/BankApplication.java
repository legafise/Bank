package com.lashkevich.bank;

import com.lashkevich.bank.command.CommandController;

public class BankApplication {

    public static void main(String[] args) {
        new CommandController().init();
    }

}
