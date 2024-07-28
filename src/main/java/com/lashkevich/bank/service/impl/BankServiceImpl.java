package com.lashkevich.bank.service.impl;

import com.lashkevich.bank.dao.AbstractDAO;
import com.lashkevich.bank.dao.BankDAO;
import com.lashkevich.bank.dao.DAORegistry;
import com.lashkevich.bank.dao.connection.transaction.BankTransaction;
import com.lashkevich.bank.dao.connection.transaction.BankTransactionManager;
import com.lashkevich.bank.entity.Account;
import com.lashkevich.bank.entity.Bank;
import com.lashkevich.bank.entity.Client;
import com.lashkevich.bank.entity.Currency;
import com.lashkevich.bank.exception.EntityNotFoundException;
import com.lashkevich.bank.service.*;

import java.math.BigDecimal;
import java.util.Optional;

public class BankServiceImpl
        extends AbstractService<Bank>
        implements BankService {

    private final BankTransactionManager transactionManager;
    private final ClientService clientService;
    private final AccountService accountService;

    public BankServiceImpl() {
        clientService = (ClientService) ServiceRegistry.CLIENT_SERVICE.getService();
        accountService = (AccountService) ServiceRegistry.ACCOUNT_SERVICE.getService();
        transactionManager = BankTransactionManager.getInstance();
    }

    @Override
    protected AbstractDAO<Bank> getDAO() {
        return (BankDAO) DAORegistry.BANK_DAO.getDAO();
    }

    @Override
    public void addClientAndOpenAccount(long clientId, long bankId, Currency currency) {
        BankTransaction transaction = transactionManager.createTransaction();
        try {
            Optional<Client> clientOptional = clientService.findById(clientId);
            if (clientOptional.isEmpty()) {
                throw new EntityNotFoundException("Unable to find client with id = " + clientId);
            }

            Optional<Bank> bankOptional = getDAO().findById(bankId);
            if (bankOptional.isEmpty()) {
                throw new EntityNotFoundException("Unable to find bank with id = " + bankId);
            }

            Account account = new Account();
            account.setBankId(bankId);
            account.setClientId(clientId);
            account.setBalance(new BigDecimal("0"));
            account.setCurrency(currency);
            accountService.save(account);

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        } finally {
            transactionManager.closeTransaction(transaction);
        }
    }

}
