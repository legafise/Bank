package com.lashkevich.bank.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public final class CommandExecutor {

    private static final Logger log = LogManager.getLogger(CommandExecutor.class);

    private static final String COMMAND_SPLITTER = " ";

    private static CommandExecutor instance;

    private CommandExecutor() {
    }

    public static CommandExecutor getInstance() {
        if (instance == null) {
            instance = new CommandExecutor();
        }

        return instance;
    }


    public void executeCommand(String commandText) {
        String[] parts = commandText.split(COMMAND_SPLITTER);
        String commandName = parts[0];
        String[] params = Arrays.copyOfRange(parts, 1, parts.length);

        Command command = CommandProvider.findCommand(commandName);
        log.info("Command '{}' was found. Start executing with params '{}'", commandName, params);
        command.execute(params);
    }

}
