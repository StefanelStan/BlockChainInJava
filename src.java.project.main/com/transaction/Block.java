package com.transaction;

import java.util.Date;
import java.util.List;

/**
 *  Unit of block; part of a block chain
 * 
 * @author Stefannel,  Jul 4, 2018
 */
public class Block
{
    /** transaction */
    private final List<BlockchainTransaction> transactions;
    
    /** merkle root */
    private final String merkleRoot;
    
    /** previous hash */
    private final String previousHash;
    
    /** nonce */
    private final int nonce;
    
    /** time stamp */
    private final long timeStamp;
    
    /** hash value */
    private final String hash;

    /**
     * Constructor.
     * @param transactions block's transactions
     * @param merkleRoot merkle root
     * @param previousHash previous hash
     * @param nonce nonce
     * @param hash hash
     */
    public Block(final List<BlockchainTransaction> transactions, final String merkleRoot, final String previousHash, final int nonce, final String hash)
    {
        this.transactions = transactions;
        this.merkleRoot = merkleRoot;
        this.previousHash = previousHash;
        this.nonce = nonce;
        this.hash = hash;
        this.timeStamp = new Date().getTime();
    }

    /**
     * Get the hash value of this block
     * @return hash value
     */
    public String getHash()
    {
        return hash;
    }
}
