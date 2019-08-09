package com.bc;

import java.util.Date;

/**
 * A block, a unit for a blockChain. We ignore the merkle root for the current step 
 *
 * @author Stefannel,  Jun 24, 2018
 */
public class Block
{
    /** Id of the block */
    private final int id;
    
    /** String of transactions */
    private final String transactions;
    
    /** Previous's block hash */
    private final String previousHash;
    
    /** Blocks's creation time */
    private final long timeStamp;

    /** The Nonce = number only used one */
    private int nonce;
    
    /** Block's hash value */
    private String blockHash;
    
    /**
     * Constructor.
     * @param id id of the block
     * @param transactions transactions
     * @param previousHash previous hash
     */
    public Block (final int id, final String transactions, final String previousHash) 
    {
        this.id = id;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
    }

    /**
     * Get block's hash value
     * @return The hash value
     */
    public String getBlockHash()
    {
        return blockHash;
    }

    /**
     * Get the nonce
     * @return the nonce
     */
    public int getNonce() 
    {
        return nonce;
    }
    
    /**
     * Get the string content of the block ready to be hashed
     * @return the block's string representation
     */
    public String getStringContent() 
    {
        return id + previousHash + Long.toString(timeStamp) + transactions;
    }

    @Override
    public String toString()
    {
        return "Block [id=" + id + ", transactions=" + transactions + ", blockHash=" + blockHash + ", previousHash=" + previousHash + "]";
    }

    /**
     * Set block hash and nonce
     * @param currentHash block hash
     * @param nonce nonce
     */
    public synchronized void setBlockHashAndNonce(final String currentHash, final int nonce)
    {
        if (this.blockHash == null) 
        {
            this.blockHash = currentHash;
            this.nonce = nonce;
        }
        
    }
        
}
