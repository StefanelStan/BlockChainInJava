package com.blockchain.unit;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.blockchain.BlockchainManager;
import com.blockchain.MemPool;
import com.blockchain.Miner;
import com.blockchain.MiningManager;
import com.blockchain.TransactionStatusManager;
import com.blockchain.UTXOManager;
import com.blockchain.impl.DefaultMiningManager;
import com.crypto.CryptographicService;
import com.transaction.BlockchainTransaction;
import com.transaction.TransactionStatus;
import com.transaction.WalletTransaction;

public class DefaultMiningManagerShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private MemPool memPool = context.mock(MemPool.class);
    private CryptographicService cryptographicService = context.mock(CryptographicService.class);
    private TransactionStatusManager transactionStatusManager = context.mock(TransactionStatusManager.class);
    private BlockchainManager blockchainManager = context.mock(BlockchainManager.class);
    private UTXOManager utxoManager = context.mock(UTXOManager.class);
    private Miner miner = context.mock(Miner.class);

    private PublicKey sender = context.mock(PublicKey.class, "sender");
    private PublicKey receiver = context.mock(PublicKey.class, "receiver");
    
    private MiningManager miningManager;

    
    @Before
    public void setup() 
    {
        miningManager = new DefaultMiningManager(cryptographicService, memPool, transactionStatusManager, blockchainManager, utxoManager,  miner);
    }
    
    @Test
    public void take_transactions_from_memPool_verify_and_give_them_to_be_mined() 
    {
        
        final List<BlockchainTransaction> bcTransactions = Arrays.asList(new BlockchainTransaction(new WalletTransaction(sender, receiver, 10), "wt1"));
        final byte[] signature = null;
        
        context.checking(new Expectations() 
        {
            {
                oneOf(memPool).getTransactions();
                will(returnValue(bcTransactions));
                
                oneOf(cryptographicService).verifyECDSASignature(with(sender), with(signature), with(any(String.class)));
                will(returnValue(true));
                
                oneOf(cryptographicService).getMerkleRoot(with(bcTransactions));
                will(returnValue("merkleRoot"));
                
                oneOf(transactionStatusManager).setTransactionStatus("wt1", TransactionStatus.VERIFIED);
            }
        });
    
        miningManager.processMining(); 
    }
}
