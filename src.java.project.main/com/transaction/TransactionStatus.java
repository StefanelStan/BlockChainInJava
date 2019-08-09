package com.transaction;

/**
 * Set the transaction status of this transaction
 *
 * @author Stefannel,  Jul 4, 2018
 */
public enum TransactionStatus
{
    /** Refused */
    REFUSED,
    /** In MemPool */
    MEMPOOL,
    /** Verified */
    VERIFIED,
    /** In block. This means the transaction has gone through all stages and it has been appended to the block! */
    INBLOCK,
    /** Transaction does not exist */
    NONEXISTENT
}

