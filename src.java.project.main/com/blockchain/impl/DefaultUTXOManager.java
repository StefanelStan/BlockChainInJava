package com.blockchain.impl;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.blockchain.UTXOManager;
import com.transaction.TransactionOutput;

/**
 *  Default impl of UTXO Manager
 *
 * @author Stefannel,  Jul 4, 2018
 */
public class DefaultUTXOManager implements UTXOManager
{
    /** UTXO map */
    private final Map<String, TransactionOutput> UTXO = new ConcurrentHashMap<>();
    
    @Override
    public void addUTXO(final TransactionOutput transactionOutput)
    {
        UTXO.put(transactionOutput.getId(), transactionOutput);        
    }
    

    @Override
    public void addUTXO(final List<TransactionOutput> transactions)
    {
        for(final TransactionOutput to : transactions) 
        {
            UTXO.put(to.getId(), to);
        }
    }

    @Override
    public int getUTXOSize()
    {
        return UTXO.size();
    }

    @Override
    public void removeUTXO(final String utxoId)
    {
        UTXO.remove(utxoId);        
    }

    @Override
    public void removeUTXO(final List<TransactionOutput> transactions)
    {
        for (final TransactionOutput to : transactions) 
        {
            UTXO.remove(to.getId());
        }
    }
    
    @Override
    public List<TransactionOutput> getTransactionOutput(final PublicKey receiver)
    {
        final List<TransactionOutput> outputs = new ArrayList<>();
        for (Map.Entry<String, TransactionOutput> utxo : UTXO.entrySet()) 
        {
            if (utxo.getValue().getReceiver().equals(receiver)) 
            {
                outputs.add(utxo.getValue());
            }
        }
        return outputs;
    }
}
