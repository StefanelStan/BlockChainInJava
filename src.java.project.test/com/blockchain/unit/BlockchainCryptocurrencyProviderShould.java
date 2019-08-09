package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.blockchain.BlockchainManager;
import com.blockchain.CryptocurrencyProvider;
import com.blockchain.MemPool;
import com.blockchain.TransactionStatusManager;
import com.blockchain.UTXOManager;
import com.blockchain.impl.BlockchainCryptocurrencyProvider;
import com.crypto.CryptographicService;
import com.transaction.BlockchainTransaction;
import com.transaction.TransactionOutput;
import com.transaction.TransactionStatus;
import com.transaction.WalletTransaction;

public class BlockchainCryptocurrencyProviderShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    final PublicKey senderPBKey = context.mock(PublicKey.class, "sender");
    final PublicKey receiverPBKey = context.mock(PublicKey.class, "receiver");
    final CryptographicService cryptographicService = context.mock(CryptographicService.class);
    final BlockchainManager blockchainManager = context.mock(BlockchainManager.class);
    final MemPool mempool = context.mock(MemPool.class);
    final UTXOManager utxoManager = context.mock(UTXOManager.class);
    final TransactionStatusManager transactionStatusManager = context.mock(TransactionStatusManager.class);
    private CryptocurrencyProvider ccProvider;
    
    @Before
    public void setup() 
    {
        ccProvider = new BlockchainCryptocurrencyProvider(cryptographicService, blockchainManager, mempool, utxoManager, transactionStatusManager);
    }
    
    @Test
    public void return_available_balance_for_a_wallet_user() 
    {
        context.checking(new Expectations() 
        {
            {
                oneOf(utxoManager).getTransactionOutput(senderPBKey);
                will(returnValue(Collections.emptyList()));
            }
        });
        
        double amount = ccProvider.calculateBalance(senderPBKey);
        
        assertThat(amount, equalTo(0.0));
    }
    
    @Test
    public void return_list_of_transaction_inputs_for_a_given_user_and_amount() 
    {
        context.checking(new Expectations() 
        {
            {
                oneOf(utxoManager).getTransactionOutput(with(senderPBKey));
                will(returnValue(Arrays.asList(new TransactionOutput(senderPBKey, 8, "p1"),
                                               new TransactionOutput(senderPBKey, 3, "p1"),
                                               new TransactionOutput(senderPBKey, 7, "p1"))));
            }
        });
        
        List<TransactionOutput> inputs = ccProvider.getTransactionInputs(senderPBKey, 10);
        assertThat(inputs, hasSize(greaterThanOrEqualTo(2)));
        double sum = 0.0;
        for (TransactionOutput to : inputs) 
        {
            assertThat(to.getReceiver(), equalTo(senderPBKey));
            sum+=to.getAmount();
        }
        assertThat(sum, greaterThanOrEqualTo(10.0));
        assertThat(sum, equalTo(11.0));
    }
    
    @Test
    public void accept_transactions_from_wallet_user_and_return_transactionId() 
    {
        final WalletTransaction walletTransaction = new WalletTransaction(senderPBKey, receiverPBKey, 20);
        final BlockchainTransaction blockchainTransaction = new BlockchainTransaction(walletTransaction, "abcde");
        
        context.checking(new Expectations() 
        {
            {
                oneOf(cryptographicService).hashFast(with(any(String.class)));
                will(returnValue("abcde"));
                
                oneOf(mempool).addTransaction(with(blockchainTransaction));
                
                oneOf(utxoManager).getTransactionOutput(with(senderPBKey));
                
                oneOf(cryptographicService).hashFast(with(any(String.class)));
                
                oneOf(transactionStatusManager).addTransactionStatusRecord(with("abcde"), with(TransactionStatus.MEMPOOL));
            }
        });
        
        assertThat(ccProvider.queueTransaction(walletTransaction), equalTo("abcde"));
    }
    
    @Test
    public void return_status_of_transaction_id_whenever_user_requests_it() 
    {
        context.checking(new Expectations() 
        {
            {
                oneOf(transactionStatusManager).getTransactionStatus(with("abc123"));
                will(returnValue(TransactionStatus.NONEXISTENT));
            }
        });
    
        assertThat(ccProvider.getTransactionStatus("abc123"), equalTo(TransactionStatus.NONEXISTENT));
    }
}
    