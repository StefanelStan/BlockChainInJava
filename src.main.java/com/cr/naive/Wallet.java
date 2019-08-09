package com.cr.naive;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bc.BlockChain;

/**
 * Wallet
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class Wallet
{
    /** wallet's public key */
    private final PublicKey publicKey;
    
    /** wallet's private key */
    private final PrivateKey privateKey;
    
    /** Constructor */
    public Wallet() 
    {
        final KeyPair keyPair = CryptoHelper.generateEllipticCurveCrypto();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    
    
    
    /**
     * Get public Key
     * @return public key
     */
    public PublicKey getPublicKey()
    {
        return publicKey;
    }
    


    /**
     * Get private key
     * @return private key
     */
    public PrivateKey getPrivateKey()
    {
        return privateKey;
    }

    /**
     * Calculate the balance of Transaction outputs
     * @return the wallet's balance
     */
    public double calculateBalance() 
    {
        double balance = 0.0;
        for (final Map.Entry<String, TransactionOutput> item : BlockChain.UTXO.entrySet()) 
        {
            final TransactionOutput output = item.getValue();
            //TO have as receiver ME
            if(output.isMine(publicKey)) 
            {
                balance +=  output.getAmount();
            }
        }
        
        return balance;
    }
    
    /**
     * Transfers the given amount to the given receiver; returning the transaction to confirm this or null if not enough money on wallet
     * @param receiver the receiver's public key
     * @param amount the amount of money to transfer
     * @return the Transaction or null 
     */
    public Transaction transferMoney(final PublicKey receiver, final double amount) 
    {
        if (calculateBalance() < amount) 
        {
            System.out.println("Invalid transaction because of not enough money...");
            return null;
        }
        
        //we store the inputs for the transaction in this list
        final List<TransactionInput> inputs = new ArrayList<>();
        
        //find the unspent transaction UTXo. BlockChain stores them all
        for (final Map.Entry<String, TransactionOutput> item : BlockChain.UTXO.entrySet()) 
        {
            final TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) 
            {
                //might as well add the whole UTXO and spare looking over this again and again but I guess it's safer for to keep UTXO whole data
                inputs.add(new TransactionInput(UTXO.getId()));
            }
        }
        
        //create a new transaction
        final Transaction newTransaction = new Transaction(publicKey, receiver, amount, inputs);
        //sign the transaction
        newTransaction.signTransaction(privateKey);
        
        return newTransaction;
    }

}
