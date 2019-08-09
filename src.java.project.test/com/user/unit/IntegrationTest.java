package com.user.unit;

import java.security.Security;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.blockchain.BlockchainManager;
import com.blockchain.CryptocurrencyProvider;
import com.blockchain.MemPool;
import com.blockchain.MiningManager;
import com.blockchain.TransactionStatusManager;
import com.blockchain.UTXOManager;
import com.blockchain.impl.BlockchainCryptocurrencyProvider;
import com.blockchain.impl.DefaultBlockchainManager;
import com.blockchain.impl.DefaultMemPool;
import com.blockchain.impl.DefaultMiningManager;
import com.blockchain.impl.DefaultTransactionStatusManager;
import com.blockchain.impl.DefaultUTXOManager;
import com.blockchain.impl.SimpleMiner;
import com.crypto.CryptographicService;
import com.crypto.DefaultCryptographicService;
import com.transaction.TransactionOutput;
import com.user.Cryptocurrency;
import com.user.Wallet;

public class IntegrationTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void beforeClass() 
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    private CryptographicService cryptographicService;
    private UTXOManager utxoManager;
    private MemPool memPool;
    private BlockchainManager blockchainManager;
    private TransactionStatusManager transactionStatusManager;
    private CryptocurrencyProvider cryptocurrencyProvider;
    private MiningManager miningManager;
    private Wallet userA;
    private Wallet userB;
    
    @Before
    public void setup() 
    {
        cryptographicService = new DefaultCryptographicService();
        utxoManager = new DefaultUTXOManager();
        memPool = new DefaultMemPool();
        blockchainManager = new DefaultBlockchainManager();
        transactionStatusManager = new DefaultTransactionStatusManager();
        cryptocurrencyProvider = new BlockchainCryptocurrencyProvider(cryptographicService, blockchainManager, memPool, utxoManager, transactionStatusManager);
        miningManager = new DefaultMiningManager(cryptographicService, memPool, transactionStatusManager, blockchainManager, utxoManager, new SimpleMiner());
        userA = new Wallet(cryptographicService);
        userA.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, cryptocurrencyProvider);
        userB = new Wallet(cryptographicService);
        userB.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, cryptocurrencyProvider);
    }
    
    @Test
    public void throw_runtime_exception_if_user_has_insufficient_money_in_wallet() 
    {
        userA = new Wallet(cryptographicService);
        userB = new Wallet(cryptographicService);
        userA.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, cryptocurrencyProvider);
        
        exception.expect(RuntimeException.class);
        exception.expectMessage("need more money!");
        
        userA.transferMoney(userB.getPublicKey(), Cryptocurrency.BITCOIN, 10);
    }
    
    @Test
    public void transfter_money_from_one_user_to_another_user_and_check_status() throws InterruptedException 
    {
        setup_genesis_utxo(userA, 500);
        System.out.println("User A balance = " + userA.calculateBalance(Cryptocurrency.BITCOIN));
        
        String transactionId = userA.transferMoney(userB.getPublicKey(), Cryptocurrency.BITCOIN, 100);
        System.out.println("User A balance = " + userA.calculateBalance(Cryptocurrency.BITCOIN));
        System.out.println("User B balance = " + userB.calculateBalance(Cryptocurrency.BITCOIN));
        
        
        System.out.println(String.format("Transaction status of %s = %s", transactionId, userA.getTransactionStatus(transactionId)));
        
        CountDownLatch latch = miningManager.processMining();
        
        latch.await();
        System.out.println("User A balance = " + userA.calculateBalance(Cryptocurrency.BITCOIN));
        System.out.println("User B balance = " + userB.calculateBalance(Cryptocurrency.BITCOIN));
        System.out.println(String.format("Transaction status of %s = %s", transactionId, userA.getTransactionStatus(transactionId)));
        
        String transactionId2 = userA.transferMoney(userB.getPublicKey(), Cryptocurrency.BITCOIN, 300);
        String transactionId3 = userB.transferMoney(userA.getPublicKey(), Cryptocurrency.BITCOIN, 100);
        latch = miningManager.processMining();
        latch.await();
        System.out.println("User A balance = " + userA.calculateBalance(Cryptocurrency.BITCOIN));
        System.out.println("User B balance = " + userB.calculateBalance(Cryptocurrency.BITCOIN));
        System.out.println(String.format("Transaction status of %s = %s", transactionId2, userB.getTransactionStatus(transactionId2)));
        System.out.println(String.format("Transaction status of %s = %s", transactionId3, userB.getTransactionStatus(transactionId3)));
    }
    
    private void setup_genesis_utxo(Wallet user, double amount) 
    {
        TransactionOutput output = new TransactionOutput(user.getPublicKey(), amount, "genesisParent", 
                                                         cryptographicService.hashFast(user.getPublicKey().toString()+ amount + "genesisParent"));
        utxoManager.addUTXO(output);
    }
    
}
