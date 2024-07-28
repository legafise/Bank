package com.lashkevich.bank.command;

import com.lashkevich.bank.command.impl.*;
import com.lashkevich.bank.exception.CommandNotFoundException;

import java.util.Arrays;

public enum CommandProvider {

    HELP("help", new HelpCommand()),
    STOP("stop", new StopCommand()),

    CLIENT_CREATE("createClient", new ClientCreateCommand()),
    CLIENT_FIND_BY_ID("findClientById", new ClientFindByIdCommand()),

    BANK_CREATE("createBank", new BankCreateCommand()),
    BANK_FIND_BY_ID("findBankById", new BankFindByIdCommand()),
    BANK_ADD_CLIENT("addClient", new BankAddClientCommand()),

    TRANSACTION_FIND_BY_ID("findTransactionById", new TransactionFindByIdCommand()),
    TRANSACTION_TRANSFER_CASH("transferCash", new TransactionTransferCashCommand()),

    ACCOUNT_FIND_BY_ID("findAccountById", new AccountFindByIdCommand()),
    ACCOUNT_TOP_UP_BALANCE("topUpBalance", new AccountTopUpBalanceCommand()),
    ;

    private final String commandName;
    private final Command command;

    CommandProvider(String commandName, Command command) {
        this.commandName = commandName;
        this.command = command;
    }

    public String getCommandName() {
        return commandName;
    }

    public Command getCommand() {
        return command;
    }


    /* Helpers */

    public static Command findCommand(String commandName) {
        return Arrays.stream(CommandProvider.values())
                .filter(command -> command.getCommandName().equalsIgnoreCase(commandName))
                .findFirst()
                .orElseThrow(() -> new CommandNotFoundException("Unknown command name: " + commandName))
                .getCommand();
    }

}
