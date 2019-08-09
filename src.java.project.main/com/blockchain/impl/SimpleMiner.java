package com.blockchain.impl;

import java.util.List;

import com.bc.Constants;
import com.blockchain.Miner;
import com.blockchain.MiningManager;
import com.crypto.CryptographicService;
import com.transaction.BlockchainTransaction;

/**
 * Simple impl of Miner
 *
 * @author Stefannel,  Jul 8, 2018
 */
public class SimpleMiner implements Miner
{
    @Override
    public void beginMining(final CryptographicService cryptograhpicService, final List<BlockchainTransaction> bcTransactions, final String merkleRoot,
            final MiningManager defaultMiningManager)
    {
        int nonce = 0;
        String hash = null;
        final String leadingZeroes = getLeadingZeroes();
        while(!Thread.interrupted()) 
        {
            hash = cryptograhpicService.hashFast(merkleRoot + nonce);
            if(hash.substring(0, Constants.DIFFICULTY).equals(leadingZeroes)) 
            {
                System.out.println(String.format("Miner %s found hash=%s for nonce=%s", this, hash, nonce));
                defaultMiningManager.foundGoldenHash(bcTransactions, merkleRoot, nonce, hash);
                break;
            }
            else 
            {
                nonce++;
            }
        }
    }
    
    /**
     * Get leading zeroes according to the difficulty
     * @return leading zeroes
     */
    private String getLeadingZeroes() 
    {
        final StringBuilder leadingZeroes = new StringBuilder();
        for (int i = 0; i < Constants.DIFFICULTY; i++) 
        {
            leadingZeroes.append('0');
        }
        return leadingZeroes.toString();
    }
}
