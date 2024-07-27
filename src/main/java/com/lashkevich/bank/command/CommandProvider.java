package com.lashkevich.bank.command;

import com.lashkevich.bank.command.impl.HelpCommand;
import com.lashkevich.bank.command.impl.StopCommand;

import java.util.Arrays;

public enum CommandProvider {

    HELP("help", new HelpCommand()),
    STOP("stop", new StopCommand()),
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
                .orElseThrow(() -> new RuntimeException("Unknown command name: " + commandName))
                .getCommand();
    }

}
