package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.command.CommandExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class StartCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(StartCommand.class);

    @Override
    public void execute(String[] params) {
        System.out.printf("Welcome to the Bank Management System." +
                " Type 'help' for the list of commands. Current time is %s \n", params[0]);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Input command: ");
                String command = scanner.nextLine();
                CommandExecutor.getInstance().executeCommand(command);
            } catch (Exception e) {
                System.err.println("An error occurred while executing the command." +
                        " Write 'help' to see a list of possible commands");
                log.error("Unexpected error occurred during command executing", e);
            }
        }
    }

}
