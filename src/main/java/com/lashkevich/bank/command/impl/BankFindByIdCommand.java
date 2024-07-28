package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Bank;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.BankService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BankFindByIdCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(BankFindByIdCommand.class);

    private final BankService bankService;

    public BankFindByIdCommand() {
        bankService = (BankService) ServiceRegistry.BANK_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 1) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long bankId = Long.parseLong(params[0]);
        Optional<Bank> bankOptional = bankService.findById(bankId);
        if (bankOptional.isPresent()) {
            System.out.printf("Bank with id '%d' was found - %s\n", bankId, bankOptional.get());
        } else {
            System.out.printf("Bank with id '%d' not found\n", bankId);
        }
    }

}
