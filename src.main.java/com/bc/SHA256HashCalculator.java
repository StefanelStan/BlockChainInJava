package com.bc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256Helper impl of HashHelper
 * 
 * @author Stefannel
 */
public class SHA256HashCalculator implements HashCalculator
{
    /** The hex table */
    private static final String HEXES = "0123456789abcdef";

    /** The message Digest */
    private final MessageDigest digest;

    /**
     *  Constructor
     */
    public SHA256HashCalculator()
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
    public String hashWithBuilder(final String data)
    {
        final byte[] raw = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexaDecimal = new StringBuilder();

        for (int i = 0; i < raw.length; i++)
        {
            final String hexa = Integer.toHexString(0xff & raw[i]);
            if (hexa.length() == 1)
            {
                hexaDecimal.append('0');
            }
            hexaDecimal.append(hexa);
        }
        return hexaDecimal.toString();

    }

    @Override
    public synchronized String hashFast(final String data)
    {
        final byte[] raw = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexaDecimal = new StringBuilder();
        for (final byte b : raw)
        {
            hexaDecimal.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hexaDecimal.toString();

    }

    @Override
    public String doubleHash(final String data)
    {
        return hashFast(hashFast(data));
    }
}
