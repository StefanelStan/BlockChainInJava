package com.blockchain.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bc.Constants;
import com.blockchain.BlockchainManager;
import com.transaction.Block;

/**
 * Default blockchain manager 
 *
 * @author Stefannel,  Jul 4, 2018
 */
public class DefaultBlockchainManager implements BlockchainManager
{
    /** Blockchain */
    private final List<Block> blockchain = new ArrayList<>();
    
    @Override
    public void addBlock(final Block block)
    {
        blockchain.add(block);
    }

    @Override
    public List<Block> getBlockchain()
    {
        return Collections.unmodifiableList(blockchain);
    }

    @Override
    public int getBlockchainSize()
    {
        return blockchain.size();
    }

    @Override
    public String getLastBlockHash()
    {
        if(blockchain.isEmpty()) 
        {
            return Constants.GENESIS_PREV_HASH;
        }
        else 
        {
            return blockchain.get(blockchain.size()-1).getHash();
        }
    }

}
