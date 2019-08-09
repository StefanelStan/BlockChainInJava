package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bc.Constants;
import com.blockchain.BlockchainManager;
import com.blockchain.impl.DefaultBlockchainManager;
import com.transaction.Block;

public class DefaultBlockchainManagerShould
{
    private BlockchainManager manager;
    
    @Before
    public void setup() 
    {
        manager = new DefaultBlockchainManager();
    }
    
    @Test
    public void add_blocks_to_blockchain() 
    {
        assertThat(manager.getBlockchainSize(), equalTo(0));
        manager.addBlock(new Block(null, null, null, 0, null));
        assertThat(manager.getBlockchainSize(), equalTo(1));
    }
    
    @Test
    public void return_blockchain() 
    {
        manager.addBlock(new Block(null, null, null, 0, null));
        List<Block> blockchain = manager.getBlockchain();
        assertThat(blockchain, hasSize(1));
    }
    
    @Test
    public void return_last_valid_block_hash() 
    {
        assertThat(manager.getLastBlockHash(), equalTo(Constants.GENESIS_PREV_HASH));
        manager.addBlock(new Block(null, null, null, 0, "abcde"));
        assertThat(manager.getLastBlockHash(), equalTo("abcde"));
    }
}
