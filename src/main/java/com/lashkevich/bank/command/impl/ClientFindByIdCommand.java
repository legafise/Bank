package com.lashkevich.bank.command.impl;

import com.lashkevich.bank.command.Command;
import com.lashkevich.bank.entity.Client;
import com.lashkevich.bank.exception.InvalidCommandException;
import com.lashkevich.bank.service.ClientService;
import com.lashkevich.bank.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ClientFindByIdCommand
        implements Command {

    private static final Logger log = LogManager.getLogger(ClientFindByIdCommand.class);

    private final ClientService clientService;

    public ClientFindByIdCommand() {
        clientService = (ClientService) ServiceRegistry.CLIENT_SERVICE.getService();
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 1) {
            throw new InvalidCommandException("Wrong parameters number");
        }

        long clientId = Long.parseLong(params[0]);
        Optional<Client> clientOptional = clientService.findById(clientId);
        if (clientOptional.isPresent()) {
            System.out.printf("Client with id '%d' was found - %s\n", clientId, clientOptional.get());
        } else {
            System.out.printf("Client with id '%d' not found\n", clientId);
        }
    }

}
