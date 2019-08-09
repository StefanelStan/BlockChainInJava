package com.transaction;

import java.security.PublicKey;
import java.util.Objects;

/**
 *  Transaction output. Part of UTXO.
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class TransactionOutput
{
    /** receiver */
    private final PublicKey receiver;
    
    /** amount */
    private final double amount;
    
    /** parent Transaction Id */
    private final String parentTransactionId;
    
    /** Hash ID */
    private final String hash;
    
    /**
     * Constructor 
     * @param receiver receiver
     * @param amount amount
     * @param parentTransactionId parent transaction id
     */
    public TransactionOutput(final PublicKey receiver, final double amount, final String parentTransactionId)
    {
        this(receiver, amount, parentTransactionId, String.valueOf(Objects.hash(receiver.toString(), amount, parentTransactionId)));
    }
    
    /**
     * Constructor.
     * @param receiver receiver
     * @param amount amount
     * @param parentTransactionId transaction id
     * @param hash hash
     */
    public TransactionOutput(final PublicKey receiver, final double amount, final String parentTransactionId, final String hash) 
    {
        this.receiver = receiver;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        this.hash = hash;
    }

    /**
     * Get receiver
     * @return PBKey of receiver
     */
    public PublicKey getReceiver()
    {
        return receiver;
    }

    /**
     * Get amount
     * @return amount
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Get ID
     * @return ID of this TO
     */
    public String getId()
    {
        return hash;
    }
}
