package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

import com.bc.Block;
import com.bc.BlockChain;

public class BlockChainShould
{
    @Test
    public void append_blocks_in_the_chain() 
    {
        BlockChain bc = new BlockChain();
        
        Block block1 = new Block(1, "transaction1", "prevHash1");
        Block block2 = new Block(2, "transaction2", "prevHash2");
        
        bc.addBlock(block1);
        bc.addBlock(block2);
        
        assertThat(bc.toString(), not(nullValue()));
    }
}
