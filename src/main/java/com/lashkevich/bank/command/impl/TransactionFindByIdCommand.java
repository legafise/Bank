package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Transaction;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.ServiceRegistry;
import com.lashkevich.bank.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class TransactionFindByIdCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(TransactionFindByIdCommand.class);

    private final TransactionService transactionService;

    public TransactionFindByIdCommand() {
        transactionService = (TransactionService) ServiceRegistry.TRANSACTION_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 1) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long transactionId = Long.parseLong(params[0]);
        Optional<Transaction> bankOptional = transactionService.findById(transactionId);
        if (bankOptional.isPresent()) {
            System.out.printf("Transaction with id '%d' was found - %s\n", transactionId, bankOptional.get());
        } else {
            System.out.printf("Transaction with id '%d' not found\n", transactionId);
        }
    }

}
