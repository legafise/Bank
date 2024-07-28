package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Bank;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.BankService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class BankCreateCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(BankCreateCommand.class);

    private final BankService bankService;

    public BankCreateCommand() {
        bankService = (BankService) ServiceRegistry.BANK_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 3) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        Bank bank = new Bank();
        bank.setName(params[0]);
        bank.setIndividualFee(new BigDecimal(params[1]));
        bank.setLegalEntityFee(new BigDecimal(params[2]));
        bankService.save(bank);

        System.out.println("Bank successfully created!");
        log.info("Bank '{}' was created", bank);
    }

}
