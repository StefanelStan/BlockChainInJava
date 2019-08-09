package com.bc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cr.naive.TransactionOutput;

/**
 *  A block chain made up on a series of blocks chained together via previous's block hash
 *
 * @author Stefannel,  Jun 24, 2018
 */
public class BlockChain
{
    /** UTXo of BlockChain */
    public static final Map<String, TransactionOutput> UTXO = new HashMap<>();
    /** Internal holder of the blocks */
    private List<Block> blockChain = new ArrayList<>();
    
    /**
     * Add a block into the chain. Only if it has been verified
     * @param block the block to add
     */
    public void addBlock(final Block block) 
    {
        blockChain.add(block);
    }
    
    /**
     * Get previous hash
     * @return hash of the previous element in block
     */
    public String getLastKnowHash() 
    {
        if(blockChain.isEmpty()) 
        {
            return Constants.GENESIS_PREV_HASH;
        }
        else 
        {
            return blockChain.get(blockChain.size() -1).getBlockHash();
        }
    }
    
    
    @Override
    public String toString()
    {
        final StringBuffer stb = new StringBuffer();
        for (final Block block : blockChain) 
        {
            stb.append(block).append("\n");
        }
        return stb.toString();
    }
}
