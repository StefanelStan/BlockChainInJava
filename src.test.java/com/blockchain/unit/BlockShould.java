package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

import com.bc.Block;

public class BlockShould
{
    @Test
    public void return_context_as_string() 
    {
        Block block1 = new Block(1, "transaction1", "prevHash1");
        Block block2 = new Block(2, "transaction2", "prevHash2");
        
        assertThat(block1.getStringContent(), not(nullValue()));
        assertThat(block2.getStringContent(), not(equalTo(block1.getStringContent())));
        
    }
}
