package com.cr.naive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bc.Constants;

/**
 *  Block Object in naive impl 
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class Block
{
    /** Block id */
    private int id;
    /** nonce */
    private int nonce;
    /**nonce */
    private long timeStamp;
    /** hash */
    private String hash;
    /** prev hash */
    private String previousHash;
    /** list of transactions */
    private List<Transaction> transactions;
    
    
    /**
     * Constructor
     * @param previousHash prev hash
     */
    public Block(final String previousHash) 
    {
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        generateHash();
        
    }

    /** Generate hash value */
    private void generateHash()
    {
        final String data = id + previousHash + timeStamp + transactions.toString() + nonce;
        this.hash = CryptoHelper.generateHash(data);
    }
    
    /** increment nonce */
    public void incrementNonce() 
    {
        nonce++;
    }
    
    /**
     * Add a transaction to block
     * @param transaction the transaction to add
     * @return true of added successfully, false otherwise
     */
    public boolean addTransaction(final Transaction transaction) 
    {
        if(transaction == null) 
        {
            return false;
        }
        
        //do not process if the block is the genesis block
        if(!previousHash.equals(Constants.GENESIS_PREV_HASH)) 
        {
            if(!transaction.verifyTransaction()) 
            {
                System.out.println("Transaction is not valid");
                return false;
            }   
        }
        
        transactions.add(transaction);
        System.out.println("Transaction is valid and added to the block" + this);
        
        return true;
    }

    /**
     * Get Hash
     * @return return hash
     */
    public String getHash()
    {
        return hash;
    }
}
