package com.yuhlearn.datastructures;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TriGramIndex
{
    private final Map<Integer, SortedArraySet> table;

    public TriGramIndex()
    {
        table = new HashMap<Integer, SortedArraySet>();
    }

    private int makeKey( byte[] chars, int i )
    {
        int key = 0x0;

        for ( int j = 0; j < 3; j++ )
        {
            key = key << 8;
            key = key ^ chars[i + j];
        }

        return key;
    }

    public void insert( String string, int value ) throws Exception
    {
        insert( string.getBytes( StandardCharsets.UTF_8 ), value );
    }

    public void insert( byte[] chars, int value ) throws Exception
    {       
        for ( int i = 0; i < chars.length - 2; i++ )
        {
            int key = makeKey( chars, i );
            put( key, value );
        } 
    }

    private void put( int key, int value ) throws Exception
    {
        SortedArraySet resultSet = table.get( key );

        if ( resultSet == null )
        {
            resultSet = new SortedArraySet();
            table.put( key, resultSet ); 
        }

        resultSet.add( value );
    }

    public SortedArraySet find( String string )
    {
        return find( string.getBytes( StandardCharsets.UTF_8 ) );
    }

    public SortedArraySet find( byte[] chars )
    {
        SortedArraySet resultSet = new SortedArraySet();

        for ( int i = 0; i < chars.length - 2; i++ )
        {
            int key = makeKey( chars, i );
            SortedArraySet currentSet = table.get( key );

            if ( currentSet == null )
                return new SortedArraySet();
            else if ( i == 0 )
                resultSet.copy( currentSet );
            else
                resultSet.intersect( currentSet );

            if ( resultSet.isEmpty() )
                break;
        }
        
        return resultSet;
    }
}