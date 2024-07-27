package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StopCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(StartCommand.class);

    @Override
    public void execute(String[] params) {
        log.info("Stopping application");
        System.out.println("Stopping..");
        // TODO: 27.07.2024 close resources
        System.exit(0);
    }

}
