package com.transaction;

/**
 * Transaction Input 
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class TransactionInput
{
    /** transaction id */
    private final String transactionOutputId;
    
    /** the TO/utxo referenced by the transaction output id */
    private final TransactionOutput utxo;
    
    /**
     * Constructor.
     * @param id id of wrapped UTXO / Transaction Output
     * @param to the wrapped UTXO / Transaction Output
     */
    public TransactionInput(final String id, final TransactionOutput to)
    {
        this.transactionOutputId = id;
        this.utxo = to;
    }

    /**
     * Get transaction id
     * @return transaction id
     */
    public String getTransactionOutputId()
    {
        return transactionOutputId;
    }

    /**
     * Get the wrapped UTXO
     * @return the wrapped UTXO / transaction output
     */
    public TransactionOutput getUtxo()
    {
        return utxo;
    }
}
