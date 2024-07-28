package com.lashkevich.bank.service.impl;

import com.lashkevich.bank.dao.AbstractDAO;
import com.lashkevich.bank.dao.DAORegistry;
import com.lashkevich.bank.dao.TransactionDAO;
import com.lashkevich.bank.dao.connection.transaction.BankTransaction;
import com.lashkevich.bank.dao.connection.transaction.BankTransactionManager;
import com.lashkevich.bank.entity.*;
import com.lashkevich.bank.exception.EntityNotFoundException;
import com.lashkevich.bank.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

public class TransactionServiceImpl
        extends AbstractService<Transaction>
        implements TransactionService {

    private static final Logger log = LogManager.getLogger(TransactionServiceImpl.class);

    private final BankTransactionManager transactionManager;
    private final AccountService accountService;
    private final ClientService clientService;
    private final BankService bankService;

    public TransactionServiceImpl() {
        accountService = (AccountService) ServiceRegistry.ACCOUNT_SERVICE.getService();
        clientService = (ClientService) ServiceRegistry.CLIENT_SERVICE.getService();
        bankService = (BankService) ServiceRegistry.BANK_SERVICE.getService();
        transactionManager = BankTransactionManager.getInstance();
    }

    @Override
    protected AbstractDAO<Transaction> getDAO() {
        return (TransactionDAO) DAORegistry.TRANSACTION_DAO.getDAO();
    }

    @Override
    public Transaction transferCash(long accountFromId, long accountToId, BigDecimal amount) {
        BankTransaction transaction = transactionManager.createTransaction();
        try {
            Optional<Account> accountFromOptional = accountService.findById(accountFromId);
            if (accountFromOptional.isEmpty()) {
                throw new EntityNotFoundException("Unable to find account with id = " + accountFromId);
            }
            Optional<Account> accountToOptional = accountService.findById(accountToId);
            if (accountToOptional.isEmpty()) {
                throw new EntityNotFoundException("Unable to find account with id = " + accountToId);
            }

            Account accountFrom = accountFromOptional.get();
            Account accountTo = accountToOptional.get();
            BigDecimal startAmount = new BigDecimal(amount.toString());

            boolean useFee = accountFrom.getBankId() != accountTo.getBankId();
            if (useFee) {
                Optional<Client> clientOptional = clientService.findById(accountFrom.getClientId());
                assert clientOptional.isPresent();
                ClientType clientFromType = clientOptional.get().getType();

                Optional<Bank> bankOptional = bankService.findById(accountFrom.getBankId());
                assert bankOptional.isPresent();
                Bank bank = bankOptional.get();

                if (clientFromType == ClientType.INDIVIDUAL) {
                    amount = amount.add(bank.getIndividualFee());
                } else if (clientFromType == ClientType.LEGAL_ENTITY) {
                    amount = amount.add(bank.getLegalEntityFee());
                }
            }

            if (accountFrom.getBalance().compareTo(amount) < 0) {
                log.warn("Account '{}' does not have enough cash on balance", accountFromId);
                throw new RuntimeException("Not enough money on balance");
            }
            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountService.update(accountFrom);

            boolean equalCurrency = accountFrom.getCurrency().equals(accountTo.getCurrency());
            if (!equalCurrency) {
                amount = amount.multiply(accountFrom.getCurrency().getRateToDollar()); // convert to dollars
                amount = amount.divide(accountTo.getCurrency().getRateToDollar(), RoundingMode.FLOOR); // convert recipient currency
            }
            accountTo.setBalance(accountTo.getBalance().add(amount));
            accountService.update(accountTo);

            Transaction transferTransaction = new Transaction();
            transferTransaction.setFromAccountId(accountFromId);
            transferTransaction.setToAccountId(accountToId);
            transferTransaction.setCurrency(accountFrom.getCurrency());
            transferTransaction.setAmount(startAmount);
            transferTransaction.setDateTime(LocalDateTime.now());
            getDAO().save(transferTransaction);

            transactionManager.commit(transaction);
            log.info("Transaction is success! Information: '{}'", transferTransaction);
            return transferTransaction;
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        } finally {
            transactionManager.closeTransaction(transaction);
        }
    }

}
