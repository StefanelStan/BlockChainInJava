package com.blockchain;

import java.security.PublicKey;
import java.util.List;

import com.transaction.WalletTransaction;
import com.transaction.TransactionOutput;
import com.transaction.TransactionStatus;

/**
 * Cryptocurrency provider. Has the Role of controlling and delegating work to BlockChainManager and UTXOManager.
 *
 * @author Stefannel,  Jul 1, 2018
 */
public interface CryptocurrencyProvider
{
    /**
     * Calculates the balance for the given user/wallet
     * 
     * @param publicKey user/wallet's public key
     * @return the balance
     */
    double calculateBalance(PublicKey publicKey);

    /**
     * Get a minimum number of transaction outputs for given user that added up are equal or higher than the given amount
     * @param publicKey user's PBKey
     * @param amount amount
     * @return list of transaction outputs
     */
    List<TransactionOutput> getTransactionInputs(PublicKey publicKey, double amount);

    /**
     * Queue the transaction for miners to pick it up from the memPool
     * @param newTransaction new transaction to queue
     * @return the transaction id
     */
    String queueTransaction(WalletTransaction newTransaction);

    /**
     * Get status of transaction
     * @param transactionId transaction id
     * @return status
     */
    TransactionStatus getTransactionStatus(String transactionId);
}
