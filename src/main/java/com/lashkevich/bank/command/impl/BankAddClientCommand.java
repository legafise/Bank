package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Currency;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.BankService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BankAddClientCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(BankAddClientCommand.class);

    private final BankService bankService;

    public BankAddClientCommand() {
        bankService = (BankService) ServiceRegistry.BANK_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 3) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long clientId = Long.parseLong(params[0]);
        long bankId = Long.parseLong(params[1]);
        Currency currency = Currency.valueOf(params[2]);
        bankService.addClientAndOpenAccount(clientId, bankId, currency);

        String message = String.format("Client '%d' was successfully added to bank '%d'." +
                " Account with currency '%s' created", clientId, bankId, currency);
        System.out.println(message);
        log.info(message);
    }

}
