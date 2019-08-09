package com.blockchain;

import com.transaction.TransactionStatus;

/**
 * Manages the transaction status
 *
 * @author Stefannel,  Jul 5, 2018
 */
public interface TransactionStatusManager
{
    /**
     * Add a transaction status to record
     * @param transactionId transaction id
     * @param transactionStatus transaction status
     */
    void addTransactionStatusRecord(String transactionId, TransactionStatus transactionStatus);
    
    /**
     * Set the status of the transactionstatus represented by the transactionId
     * @param transactionId transaction id
     * @param status status to set
     */
    void setTransactionStatus(String transactionId, TransactionStatus status);
    
    /**
     * Get transactionStatus for the given transaction id
     * @param transactionId the transaction id
     * @return TransactionStatus
     */
    TransactionStatus getTransactionStatus(String transactionId);
}
