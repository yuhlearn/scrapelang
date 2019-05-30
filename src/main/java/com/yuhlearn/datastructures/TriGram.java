package com.yuhlearn.datastructures;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import com.zaxxer.sparsebits.SparseBitSet;

public final class TriGram
{
    private final HashMap<Integer, SparseBitSet> table;
    private static final int noChars = 3; 

    public TriGram()
    {
        table = new HashMap<Integer, SparseBitSet>();
    }

    private int makeKey( byte[] chars, int i )
    {
        int key = 0x0;

        for ( int j = 0; j < noChars; j++ )
        {
            key = key << 8;
            key = key ^ chars[i + j];
        }

        return key;
    }

    private void put( int key, int value )
    {
        SparseBitSet current = table.get( key );

        if ( current != null )
        {
            current.set( value, true );
        }
        else
        {
            SparseBitSet newSet = new SparseBitSet();
            newSet.set( value, true );
            table.put( key, newSet );
        }
    }

    public void insert( String string, Integer value )
    {
        insert( string.getBytes( StandardCharsets.UTF_8 ), value );
    }

    public void insert( byte[] chars, Integer value )
    {
        for ( int i = 0; i < chars.length - ( noChars - 1 ); i++ )
        {
            int key = makeKey( chars, i );

            put( key, value );
        }
    }

    public SparseBitSet find( String string )
    {
        return find( string.getBytes( StandardCharsets.UTF_8 ) );
    }

    public SparseBitSet find( byte[] chars )
    {
        SparseBitSet resultSet = new SparseBitSet();

        for ( int i = 0; i < chars.length - ( noChars - 1 ); i++ )
        {
            int key = makeKey( chars, i );

            if ( i == 0 )
            {
                resultSet.or( table.get( key ) );
            }
            else
            {
                SparseBitSet otherSet = table.get( key );
                
                if ( otherSet != null )
                    resultSet.and( otherSet );
                else
                    resultSet.clear();
            }

            if ( resultSet.size() == 0 )
                break;
        }

        return resultSet;
    }

    public SparseBitSet get( int key )
    {
        return table.get( key );
    }

    public Set<Integer> keys()
    {
        return table.keySet();
    }

    public String className()
    {
        return table.getClass().getSimpleName();
    }
}