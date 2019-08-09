package com.bc;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Main App
 * @author Stefannel,  Jun 24, 2018
 */
public class App
{
    /**
     * Main entry point
     * @param args the args
     * @throws Exception if any occur
     */
    public static void main(final String args[]) throws Exception
    {
        final BlockChain blockChain = new BlockChain();
        final HashCalculator hashCalculator = new SHA256HashCalculator();

        final MiningSupervisor supervisor = new MiningSupervisor(blockChain);
        
        final MiningGroup miningGroup1 = new MiningGroup("MG1", 10, hashCalculator, supervisor);
        final MiningGroup miningGroup2 = new MiningGroup("MG2", 10, hashCalculator, supervisor);
        supervisor.registerMiningGroup(miningGroup1);
        supervisor.registerMiningGroup(miningGroup2);
        
        
        final Block block0 = new Block(0, "transaction0", supervisor.getLastKnowHash());
        supervisor.startMining(block0);
        //waitTillFinished(semaphore);
 
        supervisor.getSemaphore().acquire();
        final Block block1 = new Block(1, "transaction1", supervisor.getLastKnowHash());
        supervisor.getSemaphore().release();
        
        supervisor.startMining(block1);
//        //waitTillFinished(semaphore);
//        
        supervisor.getSemaphore().acquire();
        final Block block2 = new Block(2, "transaction2", supervisor.getLastKnowHash());
        supervisor.getSemaphore().release();
        supervisor.startMining(block2); 
//        //waitTillFinished(semaphore);
        
        supervisor.getSemaphore().acquire();
        System.out.println(blockChain.toString());
        supervisor.getSemaphore().release();
    }
    
    /**
     * Get some random transactions
     * @return random transactions
     */
    private static List<String> getTransactions()
    {
        final List<String> transactions = new ArrayList<>();
        
        return transactions;
    }
    
    
}
