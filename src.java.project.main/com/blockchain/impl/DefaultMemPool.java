package com.blockchain.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.blockchain.MemPool;
import com.transaction.BlockchainTransaction;

/**
 * Default MemPool impl
 *
 * @author Stefannel,  Jul 3, 2018
 */
public class DefaultMemPool implements MemPool
{
    /** Lock for access to mem pool */
    private final Semaphore semaphore = new Semaphore(1, true);
    
    /** Mem pool */
    private final List<BlockchainTransaction> memPool = new LinkedList<>();
    
    @Override
    public void addTransaction(final BlockchainTransaction transaction)
    {
        acquirePermit();
        memPool.add(transaction);
        semaphore.release();
    }

    @Override
    public int getSize()
    {
        acquirePermit();
        final int size = memPool.size();
        semaphore.release();
        return size;
    }

    @Override
    public List<BlockchainTransaction> getTransactions()
    {
        acquirePermit();
        final List<BlockchainTransaction> transactions = new LinkedList<>(memPool);
        semaphore.release();
        return transactions;
    }

    @Override
    public void removeTransactions(final List<BlockchainTransaction> transactions)
    {
        acquirePermit();
        memPool.removeAll(transactions);
        semaphore.release();
    }

    /**
     * Attempts to acquire a permit
     */
    private void acquirePermit()
    {
        try
        {
            semaphore.acquire();
        }
        catch (final InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
