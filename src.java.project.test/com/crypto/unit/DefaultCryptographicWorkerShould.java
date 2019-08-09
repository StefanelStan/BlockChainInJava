package com.crypto.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.crypto.CryptographicWorker;
import com.crypto.DefaultCryptographicWorker;
import com.transaction.BlockchainTransaction;
import com.transaction.WalletTransaction;

public class DefaultCryptographicWorkerShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private PublicKey sender = context.mock(PublicKey.class, "sender");
    private PublicKey receiver = context.mock(PublicKey.class, "receiver");
    
    private CryptographicWorker worker;
    
    @BeforeClass
    public static void beforeClass() 
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    @Before
    public void setup() 
    {
        worker = new DefaultCryptographicWorker();
    }
    
    @Test
    public void get_correct_hash_value_of_input_data() 
    {
        String expectedHash = "ac56d2314f002b1bb8e72dcace35b1805d7e58bda6033ebfbf7c35f35df4d24e";
        String expectedHash2 = "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a";
        String data = "data123";
        String data2 = "Hello world!";
        
        assertThat(worker.hashFast(data), equalTo(expectedHash));
        assertThat(worker.hashFast(data2), equalTo(expectedHash2));
    }
    
    @Test
    public void return_ECDSA_pair() 
    {
        KeyPair pair = worker.generateEllipticCurveCrypto();
        assertThat(pair, notNullValue());
        assertThat(pair.getPublic(), notNullValue());
        assertThat(pair.getPrivate(), notNullValue());
        assertThat(pair.getPrivate(), not(equalTo(pair.getPublic())));
    }
    
    @Test
    public void get_ECDSA_signature() 
    {
        KeyPair pair = worker.generateEllipticCurveCrypto();
        
        byte[] signature = worker.signWithECDSA(pair.getPrivate(), "12345");
        byte[] signature2 = worker.signWithECDSA(pair.getPrivate(), "123456");
        
        assertThat(signature, notNullValue());
        assertThat(signature2, notNullValue());
        assertThat(signature, not(equalTo(signature2)));
    }
    
    @Test
    public void verify_or_fail_ECDSA_Signature() 
    {
        KeyPair pair = worker.generateEllipticCurveCrypto();
        byte[] signature = worker.signWithECDSA(pair.getPrivate(), "12345");
        
        boolean isValid = worker.verifyECDSASignature(pair.getPublic(), signature, "12345");
        assertThat(isValid, equalTo(true));
    
        isValid = worker.verifyECDSASignature(pair.getPublic(), signature, "123456");
        assertThat(isValid, equalTo(false));
    }
    
    @Test
    public void construct_correct_merkleroot() 
    {
      
        WalletTransaction wt1 = new WalletTransaction(sender, receiver, 10);
        WalletTransaction wt2 = new WalletTransaction(sender, receiver, 20);
        
        List<BlockchainTransaction> bcTransactions = Arrays.asList(new BlockchainTransaction(wt1, "wt1"), new BlockchainTransaction(wt1, "wt2"));
        
        assertThat(worker.getMerkleRoot(bcTransactions), notNullValue());
    }
}
