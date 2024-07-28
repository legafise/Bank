package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Client;
import com.lashkevich.bank.entity.ClientType;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.ClientService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientCreateCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(StopCommand.class);

    private final ClientService clientService;

    public ClientCreateCommand() {
        clientService = (ClientService) ServiceRegistry.CLIENT_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 2) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        Client client = new Client();
        client.setName(params[0]);
        client.setType(ClientType.valueOf(params[1]));
        clientService.save(client);

        System.out.println("Client successfully created!");
        log.info("Client '{}' was created", client);
    }

}
