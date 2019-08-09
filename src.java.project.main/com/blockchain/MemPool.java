package com.blockchain;

import java.util.List;

import com.transaction.BlockchainTransaction;

/**
 * Mem Pool. Stores the temp transactions, providing options to add, remove transactions.
 *
 * @author Stefannel,  Jul 3, 2018
 */
public interface MemPool
{
    /**
     * Add a transaction to memory pool
     * @param transaction the transaction to add
     */
    void addTransaction(BlockchainTransaction transaction);
    
    /**
     * Get Mem Pool Size
     * @return size
     */
    int getSize();
    
    /**
     * Get the transactions from the mem pool
     * @return list of transactions
     */
    List<BlockchainTransaction> getTransactions();
    
    /**
     * Remove the given transactions from the mem pool
     * @param transactions transactions to remove
     */
    void removeTransactions(List<BlockchainTransaction> transactions);
}
