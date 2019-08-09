package com.bc;

/**
 *  Hashing Helper 
 * 
 * @author Stefannel
 */
public interface HashCalculator
{
    /**
     * Hash 
     * @param data the data to hash
     * @return the string as hex
     */
    String hashWithBuilder(String data);
    
    /**
     * Hash faster with a trick
     * @param data data to hash
     * @return the string as hex
     */
    String hashFast(String data);
    
    /**
     * Hash faster with a trick
     * @param data data to hash
     * @return the string as hex
     */
    String doubleHash(String data);
}
