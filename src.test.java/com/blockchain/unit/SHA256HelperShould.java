package com.blockchain.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.bc.HashCalculator;
import com.bc.SHA256HashCalculator;

/**
 *  Testing class
 *
 * @author Stefannel,  Jun 20, 2018
 */
public class SHA256HelperShould
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private HashCalculator hashHelper;
    
    @Before
    public void setup() 
    {
        hashHelper = new SHA256HashCalculator();
    }
    
    @Test
    public void return_correct_hash_for_a_string_with_builder() 
    {
        String expectedHash = "ac56d2314f002b1bb8e72dcace35b1805d7e58bda6033ebfbf7c35f35df4d24e";
        String expectedHash2 = "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a";
        String data = "data123";
        String data2 = "Hello world!";
        
        assertThat(hashHelper.hashWithBuilder(data), equalTo(expectedHash));
        assertThat(hashHelper.hashWithBuilder(data2), equalTo(expectedHash2));
    }
    
    @Test
    public void return_correct_hash_for_a_string_fast() 
    {
        String expectedHash = "ac56d2314f002b1bb8e72dcace35b1805d7e58bda6033ebfbf7c35f35df4d24e";
        String expectedHash2 = "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a";
        String data = "data123";
        String data2 = "Hello world!";
        
        assertThat(hashHelper.hashFast(data), equalTo(expectedHash));
        assertThat(hashHelper.hashFast(data2), equalTo(expectedHash2));
    }
    
    @Test
    public void return_double_hash_function() 
    {
        String expectedHash = "675de8ebf07b0ca1ed92f3cdce825df28d36d8fdc39904060d2c18b13c096edc";
        String data = "Hello world!";
        assertThat(hashHelper.doubleHash(data), equalTo(expectedHash));
    }
}
