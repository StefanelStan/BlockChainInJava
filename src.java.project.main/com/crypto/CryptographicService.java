package com.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import com.transaction.BlockchainTransaction;

/**
 * Cryptographic service
 *
 * @author Stefannel,  Jul 1, 2018
 */
public interface CryptographicService
{
    /**
     * Return the SHA256 hash value
     * @param data the input data
     * @return the SHA value
     */
    String hashFast(String data);
    
    /**
     * Generate Elliptic Curve Digital Signature Algorithm KayPair
     * @return KeyPair
     */
    KeyPair generateEllipticCurveCrypto();

    /**
     * Return the signature for the with the given key and input
     * @param privateKey the private key to sign
     * @param input the input to sign
     * @return the signature
     */
    byte[] signWithECDSA(PrivateKey privateKey, String input);

    /**
     * Verifies if the given signature has been genuinely signed by owner of the given public key against the given data
     * @param publicKey the public key
     * @param signature the signature to verify
     * @param data the given data
     * @return true if verification passed, false otherwise
     */
    boolean verifyECDSASignature(PublicKey publicKey, byte[] signature, String data);
    
    /**
     * Calculates the merkleroot for the given transactions
     * @param bcTransactions the transactions to calculate their merkle root
     * @return the merkle root
     */
    String getMerkleRoot(List<BlockchainTransaction> bcTransactions);
}
