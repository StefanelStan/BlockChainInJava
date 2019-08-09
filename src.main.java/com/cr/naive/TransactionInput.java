package com.cr.naive;

/**
 * Transaction input object 
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class TransactionInput
{
    /** every transaction input object has a transaction output object. They are both created at the time of merging several UTXo into new ones */
    private String transactionOutputId;
    
    /** Unspend Transaction Outputs. */
    private TransactionOutput UTXO;
    
    /**
     * Constructor.
     * @param transactionOutputId The Transaction output Id which binds the Input with the Output.
     */
    public TransactionInput(final String transactionOutputId) 
    {
        this.transactionOutputId = transactionOutputId;
    }

    /**
     * Set UTXO
     * @param uTXO the UTXO to set
     */
    public void setUTXO(final TransactionOutput uTXO)
    {
        UTXO = uTXO;
    }

    /**
     * This Transaction input is referencing one Transaction Output from the OTX stack. 
     * @return transaction output ID
     */
    public String getTransactionOutputId()
    {
        return transactionOutputId;
    }
    
    /**
     * Get the input amount of the OTXo
     * @return the input amount of utxo
     */
    public double getInputAmount() 
    {
        return UTXO != null ? UTXO.getAmount() : 0;
    }
    
    /**
     * Get the UTXO of the Transaction Input
     * @return UTXO
     */
    public TransactionOutput getUTXO() 
    {
        return UTXO;
    }
}
