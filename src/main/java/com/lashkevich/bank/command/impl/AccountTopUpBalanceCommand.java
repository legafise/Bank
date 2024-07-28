package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.AccountService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AccountTopUpBalanceCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(AccountTopUpBalanceCommand.class);

    private final AccountService accountService;

    public AccountTopUpBalanceCommand() {
        accountService = (AccountService) ServiceRegistry.ACCOUNT_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 2) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long accountId = Long.parseLong(params[0]);
        BigDecimal amount = new BigDecimal(params[1]);
        accountService.topUpBalance(accountId, amount);

        System.out.println("Balance was updated!");
        log.info("Account {} was replenished with {}", accountId, amount);
    }

}
