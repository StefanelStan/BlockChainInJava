package com.blockchain;

import java.security.PublicKey;
import java.util.List;

import com.transaction.TransactionOutput;

/**
 * UTXO Manager. Manages access to UTXO
 *
 * @author Stefannel,  Jul 3, 2018
 */
public interface UTXOManager
{
    /**
     * Add UTXO to BlockChain's UTXO
     * @param transactionOutput the TO to add
     */
    void addUTXO(TransactionOutput transactionOutput);

    /**
     * Add the given transactions to UTXO
     * @param transactioms the transactions to add
     */
    void addUTXO(List<TransactionOutput> transactioms);
    
    /**
     * Get UTXO Size
     * @return UTXO Size
     */
    int getUTXOSize();

    /**
     * Remove the given UTXO from BlockChain's UTXO
     * @param utxoId ID of TO to remove
     */
    void removeUTXO(String utxoId);
    
    /**
     * Remove the given list of transaction inputs from the UTXO
     * @param transactions the transaction inputs to remove
     */
    void removeUTXO(List<TransactionOutput> transactions);
    
    /**
     * Get transaction outputs for the given user
     * @param receiver PB of the receiver
     * @return the list of transaction outputs
     */
    List<TransactionOutput> getTransactionOutput(PublicKey receiver);
}
