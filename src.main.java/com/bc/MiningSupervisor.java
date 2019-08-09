package com.bc;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Unique reference object to supervise the mining process and let the threads know when to stop if the hash has been found
 *
 * @author Stefannel,  Jun 24, 2018
 */
public class MiningSupervisor
{
    /** List of registered mining groups to this supervisor */
    private List<MiningGroup> miningGroups = new ArrayList<>();
    
    /** The block chain */
    private final BlockChain blockChain;
    
    /** The block is being mined */
    private Block currentMiningBlock;
    
    /** Executor Service for internal admin */
    private final ExecutorService serviceExecutor = Executors.newCachedThreadPool();
    
    /** lock for concurrent check actions */
    private final Lock quickCheckLock = new ReentrantLock();
    
    /** Semaphore to indicate that only one single mining command should be executed at a time */
    private final Semaphore miningSemaphore = new Semaphore(1);
    
    /** Hash found */
    private boolean hashFound = false;
    
    /**
     * Constructor.
     * @param blockChain the block chain which is supervised 
     */
    public MiningSupervisor (final BlockChain blockChain) 
    {
        this.blockChain = blockChain;
    }
    
    /**
     * Register a mining group to this supervisor
     * @param miningGroup the mining group
     */
    public void registerMiningGroup(final MiningGroup miningGroup) 
    {
        miningGroups.add(miningGroup);
    }
    
    /**
     * Start mining a given block
     * @param block the given block to mine
     */
    public void startMining(final Block block) 
    {
        try
        {
            miningSemaphore.acquire();
            this.currentMiningBlock = block;
            hashFound = false;
            for (final MiningGroup mg : miningGroups) 
            {
                mg.startMining(currentMiningBlock);
            }
        }
        catch (final InterruptedException e)
        {
            System.out.println(e);
            miningSemaphore.release();
        }
    }

    /**
     * Has hash been found ?
     * @return true if yes, false otherwise
     */
    public boolean hasHashBeenFound()
    {
        quickCheckLock.lock();
        quickCheckLock.unlock();
        return hashFound;
    }

    
    /** Signals that has has been found 
     * @param miningGroup the minignGroup which found the hash
     * */
    public synchronized void iFoundTheHash(final MiningGroup miningGroup) 
    {
        if (!hashFound) 
        {
            
            hashFound = true;
            miningGroup.award();
            blockChain.addBlock(currentMiningBlock);
            stopMiningGroups();
            miningSemaphore.release();
        }
    } 
    
    /** Stop the mining groups */
    private void stopMiningGroups()
    {
        final Queue<Future<?>> futures = new ConcurrentLinkedQueue<Future<?>>();
        for(final MiningGroup mg : miningGroups) 
        {
            futures.add(serviceExecutor.submit(new Runnable() 
            {
                @Override
                public void run()
                {
                    mg.stopMining();
                }
            }));
        }
        for (Future<?> future : futures) 
        {
            try
            {
                System.out.println("Will try to future.get");
                future.get();
                System.out.println("Finished getting");
            }
            catch (final InterruptedException | ExecutionException e)
            {
               System.out.println(e);
            }
        }
        
    }

    /**
     * Get last knows Hash Code of the block
     * @return last known hash Code
     */
    public String getLastKnowHash()
    {
        quickCheckLock.lock();
        quickCheckLock.unlock();
        return blockChain.getLastKnowHash();
    }

    
    /**
     * Get semaphore
     * @return semaphore
     */
    public Semaphore getSemaphore()
    {
        return miningSemaphore;
    }
}
