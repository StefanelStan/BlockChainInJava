package com.cr.naive;

import java.security.PublicKey;
import java.util.Objects;

/**
 * Transaction output class
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class TransactionOutput
{
    /*                                  -> TransactionID 2 : buy a pc of 0.25 BTC
        [Daniel  -> me 0.1  BTC]     TI             TO
        [Adam    -> me 0.2  BTC]   [T2, T3] |  [computerShop, 0.25 BTC, 2] //sender, receiver ID by PBkey
        [Sue     -> me 0.01 BTC]            |  [me, 0.05 BTC, 2] 
    
    
    
    */
    /** Each Ti and To has a parent to which they belong */
    private String parentTransactionId;
    
    /** Hash value that is used as transaction ID; made out of receiver + ammount + parentTransaction ID */
    private String hash;
    
    /** The receiver's public key. its PB is also its identifier */
    private PublicKey receiver;

    /** The amount of the transaction */
    private double amount;

    /**
     * Constructor
     * @param receiver public key of the receiver
     * @param amount the amount
     * @param parentTransactionId parent transaction id
     */
    public TransactionOutput(final PublicKey receiver, final double amount, final String parentTransactionId)
    {
        this.parentTransactionId = parentTransactionId;
        this.receiver = receiver;
        this.amount = amount;
        
        hash = CryptoHelper.generateHash(receiver.toString() + Double.toString(amount) + parentTransactionId);
    }

    /**
     * Is this transaction mine?
     * @param publicKey the public key to verify against
     * @return true if the given public key matches transaction's public key
     */
    public boolean isMine(final PublicKey publicKey) 
    {
        return Objects.equals(receiver,publicKey);
    }
    
    /**
     * Get this transaction's output ID
     * @return the transaction's id
     */
    public String getId() 
    {
        return hash;
    }
    
    /**
     * Get the amount of this Transaction Output
     * @return amount
     */
    public double getAmount() 
    {
        return amount;
    }
    
}
