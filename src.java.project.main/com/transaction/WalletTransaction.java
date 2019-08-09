package com.transaction;

import java.security.PublicKey;
import java.util.Objects;

/**
 * Transaction
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class WalletTransaction
{
    /** Sender PBKey */
    private final PublicKey sender;
    
    /** reveier PBKey */
    private final PublicKey receiver;
    
    /** transaction amount */
    private final double amount;
    
    /** signature */
    private byte[] signature;
    
    /**
     * Constructor
     * @param sender PBKey of sender
     * @param receiver PBKey of receiver
     * @param amount amount
     */
    public WalletTransaction(final PublicKey sender, final PublicKey receiver, final double amount)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    /**
     * Get Sender's PBKey
     * @return sender's PBKey
     */
    public PublicKey getSender()
    {
        return sender;
    }

    /**
     * Get Receiver PBKey
     * @return receiver's PBKey
     */
    public PublicKey getReceiver()
    {
        return receiver;
    }

    /**
     * Get transaction's amount
     * @return the amount
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Return transaction's signature
     * @return the signature
     */
    public byte[] getSignature()
    {
        return signature;
    }

    /**
     * Creates a String ready to be hashed
     * @return string value of transaction
     */
    public String toHashString()
    {
        return sender.toString() + receiver.toString() + String.valueOf(amount);
    }

    /**
     * Set transaction signature
     * @param signature the signature
     */
    public void setSignature(final byte[] signature)
    {
        if (this.signature == null) 
        {
            this.signature = signature;
        }        
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(signature);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final WalletTransaction other = (WalletTransaction) obj;
        return this.hashCode() == other.hashCode();
    }
}
