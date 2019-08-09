package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.security.PublicKey;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.blockchain.UTXOManager;
import com.blockchain.impl.DefaultUTXOManager;
import com.transaction.TransactionOutput;

public class DefaultUTXOManagerShould
{
    private UTXOManager utxoManager;
    
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private PublicKey publicKey1 = context.mock(PublicKey.class, "publicKey1");
    private PublicKey publicKey2 = context.mock(PublicKey.class, "publicKey2");
    
    @Before
    public void setup() 
    {
        utxoManager = new DefaultUTXOManager();
    }
    
    @Test
    public void add_transaction_outputs_to_UTXO_records() 
    {
        assertThat(utxoManager.getUTXOSize(), equalTo(0));
        utxoManager.addUTXO(new TransactionOutput(publicKey1, 0, null));
        assertThat(utxoManager.getUTXOSize(), equalTo(1));
    }
    
    @Test
    public void remove_UTXO_from_record() 
    {
        //hash value aka ID =  a76200ffcc702149b4f8caaebccbdea4e67897a7f016ac107b56f4cb23196995
        utxoManager.addUTXO(new TransactionOutput(publicKey1, 1.0, "t1", "idx1"));
        utxoManager.addUTXO(new TransactionOutput(publicKey2, 2.0, "t2", "idx2"));
        assertThat(utxoManager.getUTXOSize(), equalTo(2));
        
        utxoManager.removeUTXO("idx1");
        assertThat(utxoManager.getUTXOSize(), equalTo(1));
    }
    
    @Test
    public void getUTXO_for_a_given_user() 
    {
        utxoManager.addUTXO(new TransactionOutput(publicKey1, 0, "aba"));
        utxoManager.addUTXO(new TransactionOutput(publicKey1, 0, "aca"));
        utxoManager.addUTXO(new TransactionOutput(publicKey2, 0, null));
        assertThat(utxoManager.getTransactionOutput(publicKey1), hasSize(2));
    }
}
