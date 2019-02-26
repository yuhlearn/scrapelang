package com.yuhlearn.datastructures;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class UTF8CharMap 
{
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final int MIN_BYTE_VALUE = Byte.MIN_VALUE;   
    public static final int MAX_BYTE_VALUE = Byte.MAX_VALUE;

    public static final int MIN_UTF8_VALUE = 0;   
    public static final int MAX_UTF8_VALUE = Byte.MAX_VALUE - Byte.MIN_VALUE;

    // Dummy constructor
    private UTF8CharMap() 
    {
        throw new UnsupportedOperationException();
    }

    public static int byteToInt( byte c ) 
    {
        return ( (int) c ) - Byte.MIN_VALUE;
    }
 
    public static byte intToByte( int i ) 
    {
        return (byte) ( i + Byte.MIN_VALUE );
    }
}
