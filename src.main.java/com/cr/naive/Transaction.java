package com.cr.naive;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.bc.BlockChain;

/**
 * Transaction class
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class Transaction
{
    /** This transaction id; it's not the signature but just a transaction id */
    private String transactionId;
    
    /** The sender of this transaction */
    private final PublicKey sender;
    
    /** The receiver of this transaction */
    private final PublicKey receiver;
    
    /** The amount of this transaction */
    private final double amount;

    /** Transaction's Signature using sender's private key*/
    private byte[] signature;
    
    /** Every transaction has inputs */
    private List<TransactionInput> inputs;
    
    /** Every transaction has outputs */
    private List<TransactionOutput> outputs;
    
    /**
     * Constructor.
     * @param sender the sender referenced by public key
     * @param receiver the receiver referenced by public key
     * @param amount the amount
     * @param inputs the list of inputs. 
     *  <div> if sum(inputs) > amount, the difference will be returned to sender via sender ->sender diffAmmount transaction </div>
     */
    public Transaction(final PublicKey sender, final PublicKey receiver, final double amount, final List<TransactionInput> inputs) 
    {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = new ArrayList<>();
        calculateHash();
    }

    /**
     * Calculates Hash of transaction: Sender, Receiver + amount.
     */
    private void calculateHash()
    {
        transactionId = CryptoHelper.generateHash(sender.toString() + receiver.toString() + String.valueOf(amount));
    }

    /**
     * Get transaction Id
     * @return return transaction ID
     */
    public String getTransactionId()
    {
        return transactionId;
    }

    /**
     * Get sender PBKey
     * @return sender
     */
    public PublicKey getSender()
    {
        return sender;
    }

    /**
     * Get receiver.
     * @return receiver
     */
    public PublicKey getReceiver()
    {
        return receiver;
    }

    /**
     * Get signature
     * @return the transaction's signature
     */
    public byte[] getSignature()
    {
        return signature;
    }

    /**
     * Signs the transaction with the sender's Private Key
     * @param senderPrivateKey sender's private key
     */
    public void signTransaction(final PrivateKey senderPrivateKey) 
    {
        final String inputData = sender.toString() + receiver.toString() + String.valueOf(amount);
        signature = CryptoHelper.signWithECDSA(senderPrivateKey, inputData);
    }
    
    /**
     * Verifies if the transaction is valid
     * @return true if valid, false otherwise
     */
    public boolean verifySignature() 
    {
        final String inputData = sender.toString() + receiver.toString() + String.valueOf(amount);
        return CryptoHelper.verifyECDSASignature(sender, signature, inputData);
    }

    
    /**
     * Verifies the transaction; verifyTransaction(transaction, PublicKey pbKey)
     * @return true if validation is successful, false otherwise
     */
    public boolean verifyTransaction() 
    {
        if(!verifySignature()) 
        {
            System.out.println("Invalid transaction due to invalid signature...");
            return false;
        }
        
        //Get the unspent transactions (consider the inputs)
        for (final TransactionInput input : inputs) 
        {
            input.setUTXO(BlockChain.UTXO.get(input.getTransactionOutputId()));
        }
        
        //transactions have 2 parts: send amount to receiver + send the (balance - amount) back to sender
        outputs.add(new TransactionOutput (receiver, amount, transactionId));
        outputs.add(new TransactionOutput(sender, getInputSum() - amount, transactionId));
        
        //the outputs will be inputs for other transactions so put them back into the BC UTXo list
        for(final TransactionOutput output : outputs) 
        {
            BlockChain.UTXO.put(output.getId(), output);
        }
        
        //remove the transaction inputs from BlockChain's UTXo because they've been spent
        for(final TransactionInput input : inputs) 
        {
            BlockChain.UTXO.remove(input.getUTXO().getId());
            //equal With BlockChain.UTXO.remove(input.getTransactionOutputId())
        }
        
        
        return true;
    }

    
    /**
     * Returns the available total balance of the inputs
     * @return the total sum on inputs
     */
    private double getInputSum()
    {
        double sum = 0.0;
        for (final TransactionInput input : inputs) 
        {
            sum+=input.getInputAmount();
        }
        return sum;
    }

    /**
     * Set transaction id
     * @param transactionId transaction id
     */
    public void setTransactionId(final String transactionId)
    {
        this.transactionId = transactionId;
    }

    /**
     * Get outputs
     * @return outputs
     */
    public List<TransactionOutput> getOutputs()
    {
        return outputs;
    }
    
    
    
}