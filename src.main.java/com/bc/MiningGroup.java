package com.bc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A group of miners. Generally between 0 and  max_number of miners
 *
 * @author Stefannel,  Jun 24, 2018
 */
public class MiningGroup
{
    /** Size of the group */
    private final int SIZE;
    
    /** the hash helper */
    private final HashCalculator hashHelper;
    
    /** The minind Supervisor */
    private final MiningSupervisor miningSupervisor;
    
    /** Executor service for mining jobs */
    private ExecutorService miningExecutor;
    
    /** Executor service for maintenance jobs */
    private ExecutorService serviceExecutor = Executors.newCachedThreadPool();
    
    /** current reward of the mining group. */
    private double totalReward = 0.0;
    
    /** Name */
    private final String name;
    
    /** Has been found */
    private boolean hashFound;
    
    /** Access lock */
    private Lock lock = new ReentrantLock();
    
    /** Running tasks */
    private List<Future<?>> runningTasks = new ArrayList<>();
    
    /**
     * Constructor.
     * @param name name
     * @param size size of the group
     * @param hashHelper the hash Helper
     * @param miningSupervisor the miningSupervisor 
     */
    public MiningGroup(final String name, final int size, final HashCalculator hashHelper, final MiningSupervisor miningSupervisor) 
    {
        this.name = name;
        this.SIZE = size;
        this.hashHelper = hashHelper;
        this.miningSupervisor = miningSupervisor;
        miningExecutor = Executors.newFixedThreadPool(SIZE);
    }
    
    /**
     * Get the name of the module group.
     * @return name
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Starts mining a block
     * @param block the block to mine
     */
    public void startMining(final Block block)
    {
        final StringBuilder leadingZeroes = new StringBuilder();
        for (int i = 0; i < Constants.DIFFICULTY; i++) 
        {
            leadingZeroes.append('0');
        }
        System.out.println(name +" will start mining");
        runningTasks.clear();
        hashFound = false;
        for (int i = 0; i < SIZE; i++)
        {
            final int nonceToStart = Integer.MAX_VALUE / SIZE * i;
            final int nonceToStop = Integer.MAX_VALUE / SIZE + nonceToStart;
            final Miner miner = new Miner(block, nonceToStart, nonceToStop, hashHelper, leadingZeroes.toString(), this);
            runningTasks.add(miningExecutor.submit(miner));
        }
    }

    /** Reward the mining group for the job done. The mining group can then split it between the miners..but that's another story */
    public void award()
    {
        totalReward += Constants.MINER_REWARD;
        System.out.println(name +" has found a hash and total reward is= " + totalReward);
    }


    /**
     * Has the hash been found ?
     * @return true if yes, false otherwise
     */
    public boolean hasHashBeenFound()
    {
        final boolean answer = hashFound || miningSupervisor.hasHashBeenFound();
        return answer;
    }

    /** Announce that a hash has been found in this module group */
    public void foundHash()
    {
        if (!hashFound) 
        {
            lock.lock();
            hashFound = true;
            serviceExecutor.execute(new Runnable() 
            {
                @Override
                public void run()
                {
                    miningSupervisor.iFoundTheHash(MiningGroup.this);
                }
            });
            lock.unlock();
        }
    }
    
    /** Stop mining */
    public void stopMining()
    {
        
        System.out.println(String.format("%s received shutdown command", name));
        hashFound = true;
        for (Future<?> future : runningTasks) 
        {
            try
            {
                future.get();
            }
            catch (final InterruptedException | ExecutionException e)
            {
                System.out.println(e);
            }
        }
    }
}
