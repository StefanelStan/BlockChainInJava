package com.transaction;

import java.util.List;

/**
 *  Blockchain transaction
 *
 * @author Stefannel,  Jul 5, 2018
 */
public class BlockchainTransaction extends WalletTransaction
{
    /** transaction Id*/
    private final String transactionId;
    
    /** inputs */
    protected List<TransactionOutput> inputs;
    
    /** Outputs */
    private List<TransactionOutput> outputs;
    
    /**
     * Constructor
     * @param walletTransaction the wallet transaction
     * @param transactionId the transaction id
     */
    public BlockchainTransaction(final WalletTransaction walletTransaction, final String transactionId)
    {
        super(walletTransaction.getSender(), walletTransaction.getReceiver(), walletTransaction.getAmount());
        this.transactionId = transactionId;
        this.setSignature(walletTransaction.getSignature());
    }

    /**
     * Get transaction id
     * @return transaction id
     */
    public String getTransactionId()
    {
        return transactionId;
    }

    /**
     * Set transaction inputs
     * @param inputs the inputs to set
     */
    public void setTransactionInput(final List<TransactionOutput> inputs)
    {
        this.inputs = inputs;
    }

    /**
     * Get inputs
     * @return inputs
     */
    public List<TransactionOutput> getInputs()
    {
        return inputs;
    }

    /**
     * Set transaction output
     * @param outputs outputs
     */
    public void setTransactionOutput(final List<TransactionOutput> outputs)
    {
        this.outputs = outputs;
    }

    /**
     * Get transaction outputs
     * @return transaction outputs
     */
    public List<TransactionOutput> getOutputs()
    {
        return outputs;
    }
}
