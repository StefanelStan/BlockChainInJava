package com.blockchain.unit;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.blockchain.TransactionStatusManager;
import com.blockchain.impl.DefaultTransactionStatusManager;
import com.transaction.TransactionStatus;

public class DefaultTransactionStatusManagerShould
{
    private TransactionStatusManager transactionStatusManager;
    
    @Before
    public void setup() 
    {
        transactionStatusManager = new DefaultTransactionStatusManager();
    }
    
    @Test
    public void add_and_return_transaction_statuses() 
    {
        assertThat(transactionStatusManager.getTransactionStatus("abc"), equalTo(TransactionStatus.NONEXISTENT));
        
        transactionStatusManager.addTransactionStatusRecord("def", TransactionStatus.MEMPOOL);
        transactionStatusManager.addTransactionStatusRecord("ghi", TransactionStatus.INBLOCK);
        
        assertThat(transactionStatusManager.getTransactionStatus("ghi"), equalTo(TransactionStatus.INBLOCK));
        assertThat(transactionStatusManager.getTransactionStatus("def"), equalTo(TransactionStatus.MEMPOOL));
    }
    
    @Test
    public void set_transaction_status() 
    {
        transactionStatusManager.addTransactionStatusRecord("def", TransactionStatus.MEMPOOL);
        assertThat(transactionStatusManager.getTransactionStatus("def"), equalTo(TransactionStatus.MEMPOOL));
        
        transactionStatusManager.setTransactionStatus("def", TransactionStatus.REFUSED);
        assertThat(transactionStatusManager.getTransactionStatus("def"), equalTo(TransactionStatus.REFUSED));
    }
}
