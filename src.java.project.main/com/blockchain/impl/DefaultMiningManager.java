package com.blockchain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.bc.Constants;
import com.blockchain.BlockchainManager;
import com.blockchain.MemPool;
import com.blockchain.Miner;
import com.blockchain.MiningManager;
import com.blockchain.TransactionStatusManager;
import com.blockchain.UTXOManager;
import com.crypto.CryptographicService;
import com.transaction.Block;
import com.transaction.BlockchainTransaction;
import com.transaction.TransactionStatus;

/**
 *  Default and basic impl of mining manager
 *
 * @author Stefannel,  Jul 8, 2018
 */
public class DefaultMiningManager implements MiningManager
{
    /** Cryptographic Service */
    private final CryptographicService cryptographicService;
    
    /** MemPool */
    private final MemPool memPool;
    
    /** Transaction status Manager */
    private final TransactionStatusManager transactionStatusManager;
    
    /** BC Manager */
    private final BlockchainManager blockchainManager;
    
    /** UTXO Manager */
    private final UTXOManager utxoManager;
    
    /** reward */
    private double reward;
    
    /** Access latch */
    private CountDownLatch latch;
    
    /** basic and simple miner */
    private final Miner miner;
    
    /**
     * Constructor.
     * @param cryptographicService 
     * @param memPool the mem poool
     * @param transactionStatusManager 
     * @param blockchainManager the Bc manager
     * @param utxoManager utxo Manager
     * @param miner the miner to assist the mining manager
     */
    public DefaultMiningManager(final CryptographicService cryptographicService, final MemPool memPool, 
                                final TransactionStatusManager transactionStatusManager, final BlockchainManager blockchainManager, 
                                final UTXOManager utxoManager, final Miner miner)
    {
        this.cryptographicService = cryptographicService;
        this.memPool = memPool;
        this.transactionStatusManager = transactionStatusManager;
        this.blockchainManager = blockchainManager;
        this.utxoManager = utxoManager;
        this.miner = miner;
    }

    @Override
    public CountDownLatch processMining()
    {
        final List<BlockchainTransaction> mempoolTransactions = memPool.getTransactions();
        final List<BlockchainTransaction> verifiedTransactions = getVerifiedTransactions(mempoolTransactions);
        final String merkleRoot = cryptographicService.getMerkleRoot(verifiedTransactions);
        
        new Thread(new Runnable() 
        {
            public void run() 
            {
                miner.beginMining(cryptographicService, verifiedTransactions, merkleRoot, DefaultMiningManager.this);
            }
        }).start();
        latch = new CountDownLatch(1);
        return latch;
    }

    /**
     * Returns the verified transactions from the list of transactions
     * @param transactions the transactions
     * @return the verified transactions
     */
    private List<BlockchainTransaction> getVerifiedTransactions(final List<BlockchainTransaction> transactions)
    {
        final List<BlockchainTransaction> verifiedTransactions = new ArrayList<>();
        for (final BlockchainTransaction tr : transactions) 
        {
            if(cryptographicService.verifyECDSASignature(tr.getSender(), tr.getSignature(), tr.toHashString())) 
            {
                transactionStatusManager.setTransactionStatus(tr.getTransactionId(), TransactionStatus.VERIFIED);
                verifiedTransactions.add(tr);
            }
        }
        return verifiedTransactions;
    }
 
   
    @Override
    public void foundGoldenHash(final List<BlockchainTransaction> bcTransactions, final String merkleRoot, final int nonce, final String hash) 
    {
        new Thread(new Runnable() 
        {
            public void run() 
            {
                blockchainManager.addBlock(new Block(bcTransactions, merkleRoot, blockchainManager.getLastBlockHash(), nonce, hash));
                memPool.removeTransactions(bcTransactions);
                for(final BlockchainTransaction tr : bcTransactions) 
                {
                    transactionStatusManager.setTransactionStatus(tr.getTransactionId(), TransactionStatus.INBLOCK);
                    utxoManager.removeUTXO(tr.getInputs());
                    utxoManager.addUTXO(tr.getOutputs());
                }
                reward += Constants.MINER_REWARD;
                latch.countDown();
            }
        }).start();
    }
}
