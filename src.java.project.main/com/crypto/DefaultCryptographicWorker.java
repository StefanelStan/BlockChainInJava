package com.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

import com.transaction.BlockchainTransaction;

/**
 * Def impl of crypto worker
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class DefaultCryptographicWorker implements CryptographicWorker
{
    /** The hex table */
    private static final String HEXES = "0123456789abcdef";

    /** The message digest object */
    private final MessageDigest digest;
    
    /** constructor */
    public DefaultCryptographicWorker() 
    {
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (final NoSuchAlgorithmException nsae)
        {
            throw new RuntimeException(nsae);
        }
    }
    
    @Override
    public String hashFast(final String data)
    {
        final byte[] raw = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexaDecimal = new StringBuilder();
        for (byte b : raw) 
        {
            hexaDecimal.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F));
        }
        return hexaDecimal.toString();
    }
    
    @Override
    public KeyPair generateEllipticCurveCrypto()
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
    
    @Override
    public byte[] signWithECDSA(final PrivateKey privateKey, final String input) 
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

    @Override
    public boolean verifyECDSASignature(final PublicKey publicKey, final byte[] signature, final String data)
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

    @Override
    public String getMerkleRoot(final List<BlockchainTransaction> bcTransactions)
    {
        final List<String> toString = new ArrayList<>();
        for(BlockchainTransaction transaction : bcTransactions) 
        {
            toString.add(transaction.toHashString());
        }
        if(bcTransactions.size() == 1) 
        {
            return getMergedHash(toString.get(0), toString.get(0));
        }
        return buildMerkleTree(toString).get(0);
    }

    /**
     * Builds and calculates the merkle root for the given list of values
     * @param values to values to calculate merkle tree for
     * @return the merkle tree
     */
    private List<String> buildMerkleTree(final List<String> values)
    {
        if (values.size() == 1) 
        {
            return values;
        }
        final List<String> updatedValues = new ArrayList<String>();
        for(int i=0;i<values.size() -1; i+=2) 
        {
            updatedValues.add(getMergedHash(values.get(i), values.get(i+1)));
        }
        if (values.size()%2 ==1) 
        {
            updatedValues.add(getMergedHash(values.get(values.size()-1), values.get(values.size()-1)));
        }
        
        return buildMerkleTree(updatedValues);
    }
    
    /**
     * Get the hash of 2 merged values.
     * 
     * @param leftValue the left value 
     * @param rightValue the right value
     * @return the hash of the two merged values
     */
    private String getMergedHash(final String leftValue, final String rightValue)
    {
        return hashFast(leftValue + rightValue);
    }
}
