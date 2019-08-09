package com.blockchain.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.blockchain.TransactionStatusManager;
import com.transaction.TransactionStatus;

/**
 *  Default Transaction Manager
 *
 * @author Stefannel,  Jul 5, 2018
 */
public class DefaultTransactionStatusManager implements TransactionStatusManager
{
    /** statuses */
    private final Map<String, TransactionStatus> statuses = new ConcurrentHashMap<>();
    
    @Override
    public void addTransactionStatusRecord(final String transactionId, final TransactionStatus transactionStatus)
    {
        statuses.putIfAbsent(transactionId, transactionStatus);
    }

    @Override
    public void setTransactionStatus(final String transactionId, final TransactionStatus status)
    {
        System.out.println(String.format("Status for %s has been changed to %s", transactionId, status));
        statuses.replace(transactionId, status);
    }

    @Override
    public TransactionStatus getTransactionStatus(final String transactionId)
    {
        return statuses.getOrDefault(transactionId, TransactionStatus.NONEXISTENT);
    }

}
