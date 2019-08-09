package com.cr.naive;

import java.security.Security;

import com.bc.BlockChain;
import com.bc.Constants;
import com.bc.Miner;

/**
 *  Test app
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class App2
{
    
    /**
     * Main function 
     * @param args args
     */
    public static void main(final String[] args) 
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        //Create wallet + blockCgain + single miner in network
        final Wallet userA = new Wallet();
        final Wallet userB = new Wallet();
        final Wallet lender = new Wallet();
        
        final BlockChain chain = new BlockChain();
        final Miner miner = new Miner();
        
        //create genesis transaction that transfers 500 coins to userA via ICO
        final Transaction genesisTransaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 500, null);
        genesisTransaction.signTransaction(lender.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getReceiver(), 500, "0"));
        BlockChain.UTXO.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));
        
        System.out.println("contructing genesis block");
        final Block genesis = new Block(Constants.GENESIS_PREV_HASH);
        genesis.addTransaction(genesisTransaction);
        miner.mine(genesis, chain);
        
        final Block block1 = new Block(genesis.getHash());
        System.out.println(" User a has balance " + userA.calculateBalance());
        System.out.println("User A trasnsfers 120 coins to user B");
        block1.addTransaction(userA.transferMoney(userB.getPublicKey(), 120));
        miner.mine(block1, chain);
        System.out.println("User A balance is=" + userA.calculateBalance() +" and UserB balance=" + userB.calculateBalance());
        
        final Block block2 = new Block(block1.getHash());
        System.out.println("userA sends more funds (600) to userB");
        block2.addTransaction(userA.transferMoney(userB.getPublicKey(), 600));
        miner.mine(block2, chain);
        
        final Block block3 = new Block(block2.getHash());
        System.out.println("UserB sends 110 coins to UserA");
        block3.addTransaction(userB.transferMoney(userA.getPublicKey(), 100));
        System.out.println("User A balance is=" + userA.calculateBalance() +" and UserB balance=" + userB.calculateBalance());
        miner.mine(block3, chain);
        
        System.out.println("Miner's reward = " +miner.getReward());
        
        
        
        
    }
}
