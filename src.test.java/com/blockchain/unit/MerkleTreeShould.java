package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bc.MerkleTree;

public class MerkleTreeShould
{
    @Before
    public void setup() 
    {
    }
    
    @Test
    public void construct_correct_merkle_root()
    {
        List<String> transactions = Arrays.asList("aa", "bb", "dd", "ee", "11", "22", "33", "44", "55");
        List<String> transactions2 = Arrays.asList("aa", "bb", "dd", "ee", "11", "21", "33", "44", "55");
        List<String> transactions3 = Arrays.asList("bb", "aa", "dd", "ee", "11", "21", "33", "44", "55");
        MerkleTree merkleTree = new MerkleTree(transactions);
        MerkleTree merkleTree2 = new MerkleTree(transactions2);
        MerkleTree merkleTree3 = new MerkleTree(transactions3);
        
        long time = System.currentTimeMillis();
        String merkleRoot = merkleTree.getMerkleRoot();
        long time2 = System.currentTimeMillis();
        System.out.println("merkleroot1 took "+(time2 - time) + "ms");
        
        
        String merkleRoot2 = merkleTree2.getMerkleRoot();
        long time3 = System.currentTimeMillis();
        System.out.println("merkleroot2 took "+(time3 - time2) + "ms");
        
        //change the order of 2 transaction from list of transactions
        String merkleRoot3 = merkleTree3.getMerkleRoot();
        System.out.println("merkleroot3 took "+(System.currentTimeMillis() - time3) + "ms");
        
        assertThat(merkleRoot, notNullValue());
        assertThat(merkleRoot, equalTo("135c567a470cfb4822ad7656cf2ea900e71b29b7dde76e809f059a30638e60af"));
        assertThat(merkleRoot2, equalTo("46c7fe4715b58111e520a3600c49f7cb64fa84e1a91e36bd759ff90820f7ed2e"));
        assertThat(merkleRoot3, not(equalTo(merkleRoot2)));
        
        System.out.println(System.currentTimeMillis() - time);
    }
}
