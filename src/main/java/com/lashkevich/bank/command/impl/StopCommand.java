package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.dao.connection.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StopCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(StopCommand.class);

    @Override
    public void execute(String[] params) {
        log.info("Stopping application");
        System.out.println("Stopping..");
        ConnectionPool.getInstance().closeConnections();
        System.exit(0);
    }

}
