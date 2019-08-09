package com.bc;

import java.util.ArrayList;
import java.util.List;

/**
 * Merkle Tree
 *
 * @author Stefannel,  Jun 26, 2018
 */
public class MerkleTree
{
    /** transactions */
    private List<String> transactions;
    
    /** Hash Calculator */
    private HashCalculator hashCalculator = new SHA256HashCalculator();

    /**
     * Constructor 
     * @param transactions the transactions for merkle tree
     */
    public MerkleTree(final List<String> transactions)
    {
        this.transactions = transactions;
    }
    
    /**
     * Get the merkle root
     * @return merkle root
     */
    public String getMerkleRoot()
    {
        return constructMerkleRoot(transactions).get(0);
    }

    /**
     * construct merkle root
     * @param transactions the transactions to construct
     * @return the merkle root
     */
    private List<String> constructMerkleRoot(final List<String> transactions)
    {
        if (transactions.size() ==1) 
        {
            return transactions;
        }
        final List<String> updatedValues = new ArrayList<String>();
        for(int i=0;i<transactions.size() -1; i+=2) 
        {
            updatedValues.add(getMergedHash(transactions.get(i), transactions.get(i+1)));
        }
        if (transactions.size()%2 ==1) 
        {
            updatedValues.add(getMergedHash(transactions.get(transactions.size()-1), transactions.get(transactions.size()-1)));
        }
        
        return constructMerkleRoot(updatedValues);
    }

    /**
     * Get the hash of 2 merged transactions.
     * 
     * @param transactionOne transaction one
     * @param transactionTwo transaction two
     * @return the hash of the two transactions
     */
    private String getMergedHash(final String transactionOne, final String transactionTwo)
    {
        return hashCalculator.hashFast(transactionOne + transactionTwo);
    }
    
}
