package com.blockchain;

import java.util.List;

import com.transaction.Block;

/**
 * Manages the blockchain access, maintenance & management operations
 *
 * @author Stefannel,  Jul 3, 2018
 */
public interface BlockchainManager
{
    /**
     * Append a block to the chain.
     * @param block the block to append
     */
    void addBlock(Block block);
    
    /**
     * Get the block chain
     * @return the block chain
     */
    List<Block> getBlockchain();
    
    /**
     * Get the blockchain size
     * @return blockchain size
     */
    int getBlockchainSize();

    /**
     * Get the hash of the last block in the block chain
     * @return hash of last block in blockchain
     */
    String getLastBlockHash();
}
