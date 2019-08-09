package com.bc;

/**
 *  A miner/worker
 *
 * @author Stefannel,  Jun 24, 2018
 */
public class Miner extends Thread
{
    /** Block to mine */
    private final Block block;
    
    /** The current nonce */
    private int nonce;
    
    /** Nonce fragment */
    private final int nonceToStop;
    
    /** the hashHelper */
    private final HashCalculator hashHelper;
    
    /** current Hash */
    private String currentHash;
    
    /** the leading zeroes */
    private final String leadingZeroes;
    
    /** the mining group to which this worker belongs */
    private final MiningGroup miningGroup;
    
    /**
     * Constructor.
     * @param block block value
     * @param nonce the nonce to start mining
     * @param nonceToStop the nonce to stop
     * @param hashHelper the hashHelper
     * @param leadingZeroes the leading zeroes for the hash Check
     * @param miningGroup miningGroup
     */
    public Miner(final Block block, final int nonce, final int nonceToStop, final HashCalculator hashHelper, 
                 final String leadingZeroes, final MiningGroup miningGroup)
    {
        this.block = block;
        this.nonce = nonce;
        this.nonceToStop = nonceToStop;
        this.hashHelper = hashHelper;
        this.leadingZeroes = leadingZeroes;
        this.miningGroup = miningGroup;
    }

    /** Constructor */
    public Miner()
    {
        nonceToStop =0;
        miningGroup = null;
        leadingZeroes = String.valueOf(Constants.DIFFICULTY);
        hashHelper = null;
        block = null;
    }

    @Override
    public void run()
    {
        while(!Thread.interrupted() && nonce < nonceToStop && !isGoldenHash() && !miningGroup.hasHashBeenFound()) 
        {
            nonce++;
        }
        //System.out.println("Miner " + this +" from "+miningGroup.getName()+" finished work and will exit");
    }
    
    /**
     * Has this miner found the golden hash?
     * @return true if the calculated hash is the golden one, false otherwise
     */
    private boolean isGoldenHash()
    {
        currentHash = hashHelper.hashFast(block.getStringContent() + nonce);
        final boolean goldenHash = currentHash.substring(0, Constants.DIFFICULTY).equals(leadingZeroes);
       // System.out.println(String.format("Miner %s on %s found hash %s using nonce %s", this, new Date(), currentHash, nonce));
        if(goldenHash) 
        {
            block.setBlockHashAndNonce(currentHash, nonce);
            miningGroup.foundHash();
        }
        else
        {
            try
            {
                Thread.sleep(0);
            }
            catch (final InterruptedException e)
            {
                System.out.println(e);
            }
        }
        return goldenHash;
    }

    /**
     * Mines
     * @param genesis block
     * @param bc block chain
     */
    public void mine(final com.cr.naive.Block genesis, final BlockChain bc)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * Get reward
     * @return reward
     */
    public long getReward()
    {
        return 0L;
    }
}
