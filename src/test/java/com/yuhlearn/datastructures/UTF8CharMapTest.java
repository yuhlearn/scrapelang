package com.yuhlearn.datastructures;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Unit test for the UTF8CharMap class.
 */
@RunWith(JUnit4.class)
public class UTF8CharMapTest 
{
    private byte[] characters = new byte[UTF8CharMap.MAX_UTF8_VALUE + 1];

    @Before
    public void init()
    {
        for ( int i = 0; i < characters.length; ++i ) 
        {
            characters[i] = (byte) ( i - UTF8CharMap.MIN_BYTE_VALUE );
        }
    }

    @Test
    public void testCharMap()
    {
        assertEquals( characters.length-1, UTF8CharMap.MAX_UTF8_VALUE );

        int prev = UTF8CharMap.MIN_BYTE_VALUE - 1;
        for ( int i = 0; i < characters.length; ++i ) 
        {
            assertEquals( characters[i], UTF8CharMap.intToByte(UTF8CharMap.byteToInt(characters[i])) );
            assertEquals( prev, ((int) characters[i])-1 );
            assertNotSame( prev, (int) characters[i] );
            prev = (int) characters[i];
        }
    }
}
