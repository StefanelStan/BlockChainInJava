package com.crypto.naive.unit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import java.security.KeyPair;
import java.security.Security;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cr.naive.CryptoHelper;

public class CryptoHelperShould
{
    CryptoHelper cryptoHelper;
    
    @BeforeClass
    public static void beforeClass() 
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    @Before
    public void setup() 
    {
       cryptoHelper = new CryptoHelper();
    }
    
    @Test
    public void prove_elliptic_curve() 
    {
        KeyPair pair = cryptoHelper.generateEllipticCurveCrypto();
        assertThat(pair, notNullValue());
    }
    
    @Test
    public void sign_and_verify_signature() 
    {
        KeyPair pair = cryptoHelper.generateEllipticCurveCrypto();
        String message = "ABCDE12345";
        
        byte[] signWithECDSA = cryptoHelper.signWithECDSA(pair.getPrivate(), message);
        assertThat(signWithECDSA.length, greaterThan(0));
        
        boolean verification = cryptoHelper.verifyECDSASignature(pair.getPublic(), signWithECDSA, message);
        assertThat(verification, equalTo(true));
    }
}
