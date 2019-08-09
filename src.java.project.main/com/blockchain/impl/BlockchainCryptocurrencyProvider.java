package com.blockchain.impl;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.blockchain.BlockchainManager;
import com.blockchain.CryptocurrencyProvider;
import com.blockchain.MemPool;
import com.blockchain.TransactionStatusManager;
import com.blockchain.UTXOManager;
import com.crypto.CryptographicService;
import com.transaction.WalletTransaction;
import com.transaction.BlockchainTransaction;
import com.transaction.TransactionOutput;
import com.transaction.TransactionStatus;

/**
 * Blockchain cryptocurrency provider 
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class BlockchainCryptocurrencyProvider implements CryptocurrencyProvider
{
    /** crypto service */
    private final CryptographicService cryptographicService;
    
    /** The blockchain manager */
    private final BlockchainManager blockchainManager;
    
    /** the Mem pool */
    private final MemPool memPool;
    
    /** The UTXO Manager */
    private final UTXOManager utxoManager;
    
    /** Transaction status manager */
    private final TransactionStatusManager transactionStatusManager;
    
    /**
     * Constructor.
     * @param cryptographicService the crypto service
     * @param blockchainManager blockchain manager
     * @param memPool mem pool
     * @param utxoManager utxoManager
     * @param transactionStatusManager  transaction status manager
     */
    public BlockchainCryptocurrencyProvider(final CryptographicService cryptographicService, final BlockchainManager blockchainManager, 
                                            final MemPool memPool, final UTXOManager utxoManager, final TransactionStatusManager transactionStatusManager)
    {
        this.cryptographicService = cryptographicService;
        this.blockchainManager = blockchainManager;
        this.memPool = memPool;
        this.utxoManager = utxoManager;
        this.transactionStatusManager = transactionStatusManager;
    }

    @Override
    public double calculateBalance(final PublicKey publicKey)
    {
        double balance = 0;
        final List<TransactionOutput> outputs = utxoManager.getTransactionOutput(publicKey);
        for(final TransactionOutput to : outputs) 
        {
            balance += to.getAmount();
        }
        return balance;
    }

    @Override
    public List<TransactionOutput> getTransactionInputs(final PublicKey publicKey, final double amount)
    {
        double sum = 0.0;
        final List<TransactionOutput> inputs = new ArrayList<>();
        for (TransactionOutput to : utxoManager.getTransactionOutput(publicKey)) 
        {
            if (sum <= amount) 
            {
                sum += to.getAmount();
                inputs.add(to);
            }
            else 
            {
                break;
            }
        }
        return inputs;
    }

    @Override
    public String queueTransaction(final WalletTransaction newTransaction)
    {
        final String bcTransactionId = cryptographicService.hashFast(newTransaction.toHashString());
        final BlockchainTransaction bcTransaction = new BlockchainTransaction(newTransaction, bcTransactionId);
        //set the transaction input and outputs for the BCTransaction
        setTransactionInputsAndOutputs(bcTransaction);
        
        memPool.addTransaction(bcTransaction);
        transactionStatusManager.addTransactionStatusRecord(bcTransactionId, TransactionStatus.MEMPOOL);
        return bcTransactionId;
    }

    @Override
    public TransactionStatus getTransactionStatus(final String transactionId)
    {
        return transactionStatusManager.getTransactionStatus(transactionId);
    }

    /**
     * Sets transaction input and output for the BC transaction
     * @param transaction the blockchain transaction
     */
    private void setTransactionInputsAndOutputs(final BlockchainTransaction transaction)
    {
        //we store the inputs for the transaction in this list
        final List<TransactionOutput> inputs = getTransactionInputs(transaction.getSender(), transaction.getAmount());
        transaction.setTransactionInput(inputs);
        double sum = 0;
        for (final TransactionOutput to : inputs) 
        {
            sum += to.getAmount();
        }
        final List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(new TransactionOutput(transaction.getReceiver(), transaction.getAmount(), transaction.getTransactionId(), 
                                          cryptographicService.hashFast(transaction.getReceiver().toString()+transaction.getAmount()+transaction.getTransactionId())));
        if(sum > transaction.getAmount()) 
        {
            outputs.add(new TransactionOutput(transaction.getSender(), sum - transaction.getAmount(), transaction.getTransactionId()));
        }
        transaction.setTransactionOutput(outputs);
    }
}
