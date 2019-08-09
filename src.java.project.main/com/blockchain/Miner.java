package com.blockchain;

import java.util.List;

import com.crypto.CryptographicService;
import com.transaction.BlockchainTransaction;

/**
 * Worker Unit for mining
 *
 * @author Stefannel,  Jul 8, 2018
 */
public interface Miner
{
    /**
     * Begins mining on the given transactions
     * @param cryptograhpicService the cryptographic service
     * @param bcTransactions the transactions to mine
     * @param merkleRoot merkle root of the given transactions
     * @param defaultMiningManager 
     */
    void beginMining(CryptographicService cryptograhpicService, List<BlockchainTransaction> bcTransactions, 
                     String merkleRoot, MiningManager defaultMiningManager);
    
}
