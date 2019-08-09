package com.blockchain.unit;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.blockchain.MiningManager;
import com.blockchain.impl.SimpleMiner;
import com.crypto.CryptographicService;
import com.transaction.BlockchainTransaction;
import com.transaction.WalletTransaction;

public class SimpleMinerShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private MiningManager miningManager = context.mock(MiningManager.class);
    private CryptographicService cryptographicService = context.mock(CryptographicService.class);
    
    private PublicKey sender = context.mock(PublicKey.class, "sender");
    private PublicKey receiver = context.mock(PublicKey.class, "receiver");
    
    @Test
    public void find_correct_hash_for_given_transactions_and_inform_mining_manager()
    {
        WalletTransaction wt = new WalletTransaction(sender, receiver, 10);
        List<BlockchainTransaction> bcTransactions = Arrays.asList(new BlockchainTransaction(wt, "wt"));
        
        context.checking(new Expectations() 
        {
            {
                oneOf(cryptographicService).hashFast(with(any(String.class)));
                will(returnValue("0000001"));
            
                oneOf(miningManager).foundGoldenHash(with(bcTransactions), with("merkleRoot"),with(0), with("0000001"));
            }
        });
        
        new SimpleMiner().beginMining(cryptographicService, bcTransactions, "merkleRoot", miningManager);
    }
}   
