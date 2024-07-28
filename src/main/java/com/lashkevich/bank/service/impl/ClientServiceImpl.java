package com.lashkevich.bank.service.impl;

import com.lashkevich.bank.dao.AbstractDAO;
import com.lashkevich.bank.dao.ClientDAO;
import com.lashkevich.bank.dao.DAORegistry;
import com.lashkevich.bank.entity.Client;
import com.lashkevich.bank.service.AbstractService;
import com.lashkevich.bank.service.ClientService;

public class ClientServiceImpl
        extends AbstractService<Client>
        implements ClientService {

    public ClientServiceImpl() {
    }

    @Override
    protected AbstractDAO<Client> getDAO() {
        return (ClientDAO) DAORegistry.CLIENT_DAO.getDAO();
    }

}
