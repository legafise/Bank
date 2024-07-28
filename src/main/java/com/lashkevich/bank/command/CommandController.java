package com.lashkevich.bank.command;

import com.lashkevich.bank.dao.connection.ConnectionPool;
import com.lashkevich.bank.exception.CommandNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CommandController {

    private static final Logger log = LogManager.getLogger(CommandController.class);

    private final CommandExecutor commandExecutor = CommandExecutor.getInstance();

    public void init() {
        System.out.println("Welcome to the Bank Management System. Type 'help' for the list of commands.");

        ConnectionPool.getInstance().initializeConnectionPool(5);
        Scanner scanner = new Scanner(System.in);
        createScannerShutdownHook(scanner);

        while (true) {
            System.out.print("Input command: ");
            try {
                String command = scanner.nextLine();
                commandExecutor.executeCommand(command);
            } catch (CommandNotFoundException e) {
                System.err.println("Command not valid." +
                        " Write 'help' to see a list of possible commands");
                log.error("Command not valid.", e);
            } catch (Exception e) {
                System.err.println("An error occurred while executing the command." +
                        " Write 'help' to see a list of possible commands");
                log.error("Unexpected error occurred during command executing", e);
            }
        }
    }


    /* Helpers */

    private void createScannerShutdownHook(Scanner scanner) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Scanner shutdown hook is running...");
            if (scanner != null) {
                scanner.close();
                System.out.println("Scanner closed.");
            }
        }));
    }

}
