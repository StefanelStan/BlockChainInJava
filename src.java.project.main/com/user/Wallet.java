package com.user;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.blockchain.CryptocurrencyProvider;
import com.crypto.CryptographicService;
import com.transaction.TransactionStatus;
import com.transaction.WalletTransaction;

/**
 * Wallet implementation 
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class Wallet
{
    /** CC provider ..could be a list of providers */
    private final Map<Cryptocurrency, CryptocurrencyProvider> currencyProvider = new HashMap<>();
    
    /** Crypto Service */
    private final CryptographicService cryptographicService;
    
    /** transaction history */
    private final Map<String, Cryptocurrency> transactionHistory = new HashMap<>();
    
    /** Wallet's public key */
    private final PublicKey publicKey;
    
    /** wallet's private key */
    private final PrivateKey privateKey;
    
    /**
     * Constructor
     * @param cryptoService trusted cryptographic service
     */
    public Wallet(final CryptographicService cryptoService) 
    {
        this.cryptographicService = cryptoService;
        final KeyPair keyPair = cryptoService.generateEllipticCurveCrypto();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    
    /**
     * Get Wallet's Public Key
     * @return public key
     */
    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    /**
     * Add cryptocurrency provider
     * @param cryptocurrency type of cryptocurrency
     * @param ccProvider cryptocurrency provider
     */
    public void addCryptocurrencyProvider(final Cryptocurrency cryptocurrency, final CryptocurrencyProvider ccProvider)
    {
        if (currencyProvider.containsKey(cryptocurrency)) 
        {
            throw new RuntimeException("Unable to add more than one Cryptocurrency provider of the same kind");
        }
        currencyProvider.put(cryptocurrency, ccProvider);
    }

    /**
     * Calculate balance for the given cryptocurrency
     * @param cryptocurrency the given cryptocurrency
     * @return the balance
     */
    public double calculateBalance(final Cryptocurrency cryptocurrency)
    {
        final CryptocurrencyProvider ccProvider = currencyProvider.get(cryptocurrency);
        if (ccProvider == null)
        {
            return 0;
        }
        return ccProvider.calculateBalance(publicKey);
    }
    
    /**
     * Get transaction status for the givent transaction id
     * @param transactionId  the transaction id
     * @return the status
     */
    public TransactionStatus getTransactionStatus(final String transactionId) 
    {
        final Cryptocurrency cryptocurrency = transactionHistory.get(transactionId);
        if(cryptocurrency == null) 
        {
            return TransactionStatus.NONEXISTENT;
        }
        final CryptocurrencyProvider cryptocurrencyProvider = currencyProvider.get(cryptocurrency);
        if (cryptocurrencyProvider == null) 
        {
            return TransactionStatus.NONEXISTENT;
        }
        return cryptocurrencyProvider.getTransactionStatus(transactionId);
    
    }
    
    /**
     * Attempts to make a transfer to the given receiver for the given cryptocurrency and amount
     * @param receiver receiver identified by the Public Key
     * @param cryptocurrency the cryptocurrency
     * @param amount the amount
     * @return the transaction id
     */
    public String transferMoney(final PublicKey receiver, final Cryptocurrency cryptocurrency, final double amount) 
    {
        final CryptocurrencyProvider ccProvider = getCryptocurrencyProvider(cryptocurrency);
        if (ccProvider.calculateBalance(publicKey) < amount) 
        {
            throw new RuntimeException("need more money!");
        }
        
        //create a new transaction
        final WalletTransaction newTransaction = new WalletTransaction(publicKey, receiver, amount);
        //sign the transaction
        newTransaction.setSignature(cryptographicService.signWithECDSA(privateKey, newTransaction.toHashString()));
        
        final String transactionId = ccProvider.queueTransaction(newTransaction);
        transactionHistory.put(transactionId, cryptocurrency);
        return transactionId;
    }

    /**
     * Get the registered cryptocurrency provider for the given cryptocurrency
     * @param cryptocurrency the cryptocurrency
     * @return the registered provider
     */
    private CryptocurrencyProvider getCryptocurrencyProvider(final Cryptocurrency cryptocurrency)
    {
        if (cryptocurrency == null || !currencyProvider.containsKey(cryptocurrency)) 
        {
            throw new RuntimeException("Non existent/registered cryptocurrency provider for "+ cryptocurrency);
        }
        return currencyProvider.get(cryptocurrency);
    }
    
    
}
