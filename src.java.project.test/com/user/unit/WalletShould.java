package com.user.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.blockchain.CryptocurrencyProvider;
import com.crypto.CryptographicService;
import com.user.Cryptocurrency;
import com.user.Wallet;


public class WalletShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CryptocurrencyProvider ccProvider = context.mock(CryptocurrencyProvider.class);
    
    private CryptographicService cryptoService = context.mock(CryptographicService.class);
    
    private final PublicKey publicKey = context.mock(PublicKey.class);
    
    private final PrivateKey privateKey = context.mock(PrivateKey.class);
    
    private Wallet wallet;
    
    @Before
    public void setup() 
    {
        context.checking(new Expectations() 
        {
            {
                oneOf(cryptoService).generateEllipticCurveCrypto();
                will(returnValue(new KeyPair(publicKey, privateKey)));
            }
        });
        
        wallet = new Wallet(cryptoService);
    }
    
    @Test
    public void return_0_if_no_cc_provider_exists_for_given_balance() 
    {
        assertThat(wallet.calculateBalance(Cryptocurrency.BITCOIN), equalTo(0.0));
    }
    
    @Test
    public void call_the_cc_provider_to_get_the_correct_balance() 
    {
        context.checking(new Expectations() 
        {
            {
                oneOf(ccProvider).calculateBalance(with(publicKey));
                will(returnValue(20.0));
            }
        });
    
        wallet.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, ccProvider);
        
        assertThat(wallet.calculateBalance(Cryptocurrency.BITCOIN), equalTo(20.0));
    }
    
    @Test
    public void throw_runtimeException_if_one_provider_already_exists_for_given_currency() 
    {
        wallet.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, ccProvider);
        
        exception.expect(RuntimeException.class);
        wallet.addCryptocurrencyProvider(Cryptocurrency.BITCOIN, ccProvider);
    }
}
