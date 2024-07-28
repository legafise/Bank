package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Transaction;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.ServiceRegistry;
import com.lashkevich.bank.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class TransactionTransferCashCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(TransactionTransferCashCommand.class);

    private final TransactionService transactionService;

    public TransactionTransferCashCommand() {
        transactionService = (TransactionService) ServiceRegistry.TRANSACTION_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 3) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long accountFromId = Long.parseLong(params[0]);
        long accountToId = Long.parseLong(params[1]);
        BigDecimal amount = new BigDecimal(params[2]);

        Transaction transaction = transactionService.transferCash(accountFromId, accountToId, amount);
        System.out.println("Cash transferred! Created transaction - " + transaction);
    }

}
