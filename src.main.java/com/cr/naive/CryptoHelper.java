package com.cr.naive;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;

import com.bc.SHA256HashCalculator;

/**
 *  Crypt Helper 
 *
 * @author Stefannel,  Jun 28, 2018
 */
public class CryptoHelper
{
    /**
     * Generate Hash
     * @param data data to Hash
     * @return value
     */
    public static String generateHash(final String data) 
    {
        return new SHA256HashCalculator().hashFast(data);
    }
    
    /**
     * Return a keyPair object. We then sign a 
     * @return KeyPair object
     */
    public static KeyPair generateEllipticCurveCrypto() 
    {
        try 
        {
            //Elliptic Curve Digital Sign Algorithm from Bouncy Castle
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            //Strong number generator (not smth like rand.nextInt(100);
            final SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // an 192 bits prime number
            final ECGenParameterSpec params = new ECGenParameterSpec("secp192k1");
            
            keyPairGenerator.initialize(params, secureRandom);
            return keyPairGenerator.generateKeyPair();
            
        }
        catch (final Exception e) 
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
     * Signs the input with the given private key 
     * @param privateKey the private key
     * @param input the input
     * @return the signature
     */
    public static byte[] signWithECDSA(final PrivateKey privateKey, final String input) 
    {
        byte[] output = new byte[0];
        try
        {
            final Signature signature = Signature.getInstance("ECDSA", "BC");
            //init with the private key
            signature.initSign(privateKey);
            //add the data to be signed
            signature.update(input.getBytes(StandardCharsets.UTF_8));
            //generate the signature
            output = signature.sign();
        }
        catch (final NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e)
        {
            System.out.println(e);
        }
        return output;
    } 
    
    /**
     * Verifies using the provided Public Key if the signature is a legit signature of the provided data
     * @param publicKey the public key
     * @param signature the signature
     * @param data the data to be verified
     * @return true or false
     */
    public static boolean verifyECDSASignature(final PublicKey publicKey, final byte[] signature, final String data) 
    {
        try
        {
            final Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(data.getBytes(StandardCharsets.UTF_8));
            return ecdsaSignature.verify(signature);
        }
        catch (final NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e)
        {
            System.out.println(e);
            return false;
        }
    }
    
}
