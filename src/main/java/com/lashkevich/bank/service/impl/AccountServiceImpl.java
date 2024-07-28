package com.lashkevich.bank.service.impl;

import com.lashkevich.bank.dao.AbstractDAO;
import com.lashkevich.bank.dao.AccountDAO;
import com.lashkevich.bank.dao.DAORegistry;
import com.lashkevich.bank.dao.connection.transaction.BankTransaction;
import com.lashkevich.bank.dao.connection.transaction.BankTransactionManager;
import com.lashkevich.bank.entity.Account;
import com.lashkevich.bank.exception.EntityNotFoundException;
import com.lashkevich.bank.service.AbstractService;
import com.lashkevich.bank.service.AccountService;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountServiceImpl
        extends AbstractService<Account>
        implements AccountService {

    private final BankTransactionManager transactionManager;

    public AccountServiceImpl() {
        transactionManager = BankTransactionManager.getInstance();
    }

    @Override
    protected AbstractDAO<Account> getDAO() {
        return (AccountDAO) DAORegistry.ACCOUNT_DAO.getDAO();
    }

    @Override
    public void topUpBalance(long accountId, BigDecimal amount) {
        BankTransaction transaction = transactionManager.createTransaction();
        try {
            Optional<Account> accountOptional = getDAO().findById(accountId);
            if (accountOptional.isEmpty()) {
                throw new EntityNotFoundException("Unable to find account with id " + accountId);
            }

            Account account = accountOptional.get();
            account.setBalance(account.getBalance().add(amount));
            getDAO().update(account);

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        } finally {
            transactionManager.closeTransaction(transaction);
        }
    }

}
