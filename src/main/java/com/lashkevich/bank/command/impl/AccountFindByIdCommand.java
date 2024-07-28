package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Account;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.AccountService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AccountFindByIdCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(AccountFindByIdCommand.class);

    private final AccountService accountService;

    public AccountFindByIdCommand() {
        accountService = (AccountService) ServiceRegistry.ACCOUNT_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 1) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long accountId = Long.parseLong(params[0]);
        Optional<Account> bankOptional = accountService.findById(accountId);
        if (bankOptional.isPresent()) {
            System.out.printf("Account with id '%d' was found - %s\n", accountId, bankOptional.get());
        } else {
            System.out.printf("Account with id '%d' not found\n", accountId);
        }
    }

}
