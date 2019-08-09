package com.blockchain;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.transaction.BlockchainTransaction;

/**
 *  Has the role of supervising mining. It proxies the miner's wishes onto operations such as taking from memPool, verifying, adding blocks to mempool,
 *  Ideally the mingProcess would be done as: 1..n miners that guess the hash; once it happens, the manager of that group will do the rest of operations
 *  For the sake of idea, it will only use one miner and not a pool just to simplify the work 
 *
 * @author Stefannel,  Jul 8, 2018
 */
public interface MiningManager
{
    
    /**
     * Start the countdown process
     * @return latch
     */
    CountDownLatch processMining();

    /**
     * One of the miners found the holden hash
     * @param bcTransactions the transactions which have been executed
     * @param merkleRoot the merkle root
     * @param nonce the nonce
     * @param hash the hash
     */
    void foundGoldenHash(List<BlockchainTransaction> bcTransactions, String merkleRoot, int nonce, String hash);
}
