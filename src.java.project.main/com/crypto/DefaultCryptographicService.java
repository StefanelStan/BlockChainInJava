package com.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.transaction.BlockchainTransaction;

/**
 * Default impl of cryptographic service
 *
 * @author Stefannel,  Jul 1, 2018
 */
public class DefaultCryptographicService implements CryptographicService
{
    /** the worker pool */
    private final BlockingQueue<CryptographicWorker> workerPool;
    
    /** Constructor */
    public DefaultCryptographicService()
    {
        workerPool = new ArrayBlockingQueue<>(30, true);
        for (int i = 0; i < 30; i++)
        {
            workerPool.offer(new DefaultCryptographicWorker());
        }
    }
    
    @Override
    public String hashFast(final String data)
    {
        final CryptographicWorker worker = getAvailableWorker();
        final String answer = worker.hashFast(data);
        workerPool.offer(worker);
        return answer;
    }
    
    @Override
    public KeyPair generateEllipticCurveCrypto()
    {
        final CryptographicWorker worker = getAvailableWorker();
        final KeyPair keyPair = worker.generateEllipticCurveCrypto();
        workerPool.offer(worker);
        return keyPair;
    }

    @Override
    public byte[] signWithECDSA(final PrivateKey privateKey, final String input)
    {
        final CryptographicWorker worker = getAvailableWorker();
        final byte[] signature = worker.signWithECDSA(privateKey, input);
        workerPool.offer(worker);
        return signature;
    }


    @Override
    public boolean verifyECDSASignature(final PublicKey publicKey, final byte[] signature, final String data)
    {
        final CryptographicWorker worker = getAvailableWorker();
        final boolean answer = worker.verifyECDSASignature(publicKey, signature, data);
        workerPool.offer(worker);
        return answer;
    }

    /**
     * Get one available cryptographic worker from queue
     * @return one available cryptographic worker
     */
    private CryptographicWorker getAvailableWorker() 
    {
        try 
        {
            return workerPool.take();
        }
        catch (final InterruptedException e) 
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMerkleRoot(final List<BlockchainTransaction> bcTransactions)
    {
        final CryptographicWorker worker = getAvailableWorker();
        final String value = worker.getMerkleRoot(bcTransactions);
        workerPool.offer(worker);
        return value;
    }
}
