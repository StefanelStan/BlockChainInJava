package com.blockchain.unit;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import java.util.Arrays;
import java.util.List;

import com.blockchain.MemPool;
import com.blockchain.impl.DefaultMemPool;
import com.transaction.BlockchainTransaction;
import com.transaction.WalletTransaction;

public class DefaultMemPoolShould
{
    private MemPool memPool;
    
    @Before
    public void setup() 
    {
        memPool = new DefaultMemPool();
    }
    
    @Test
    public void return_correct_size() 
    {
        assertThat(memPool.getSize(), equalTo(0));
    }
    
    @Test
    public void add_transaction_to_mem_pool() 
    {
        assertThat(memPool.getSize(), equalTo(0));
        memPool.addTransaction(new BlockchainTransaction(new WalletTransaction(null, null, 0), null));
        assertThat(memPool.getSize(), equalTo(1));
    }
    
    @Test
    public void remove_transaction_from_mem_pool() 
    {
        memPool.addTransaction(new BlockchainTransaction(new WalletTransaction(null, null, 0), null));
        assertThat(memPool.getSize(), equalTo(1));
        memPool.removeTransactions(Arrays.asList(new BlockchainTransaction(new WalletTransaction(null, null, 0), null)));
        assertThat(memPool.getSize(), equalTo(0));
    }
    
    @Test
    public void get_transaction_from_memPool() 
    {
        memPool.addTransaction(new BlockchainTransaction(new WalletTransaction(null, null, 0), null));
        List<BlockchainTransaction> transactions = memPool.getTransactions();
        memPool.addTransaction(new BlockchainTransaction(new WalletTransaction(null, null, 1), null));
        
        assertThat(transactions, hasSize(1));
        assertThat(memPool.getTransactions(), hasSize(2));
    }    
}
