package com.yuhlearn.datastructures;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Unit test for the DoubleArrayTrie class.
 */
@RunWith( JUnit4.class )
public class DoubleArrayTrieTest 
{
    String[] strings = new String[100000];

    @Before
    public void init()
    {        
        String path = "/strings.txt";

        try
        {
            InputStream in = getClass().getResourceAsStream( path );
            BufferedReader reader = new BufferedReader( new InputStreamReader( in, "UTF-8" ) );

            for ( int i = 0; i < strings.length; i++ )
                strings[i] = reader.readLine();

            reader.close();
        }
        catch ( Exception e )
        {
            System.out.println( e );
        }
    }        

    @Test
    public void testInsert()
    {
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>();
        
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }
        
        for ( int i = 0; i < strings.length; i++ )
        {
            assertEquals( strings[i], trie.findEqual( strings[i] ).pollFirst() );
        }

        // Try inserting the same strings again, but with new data
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[strings.length-i-1] );
        }
        
        for ( int i = 0; i < strings.length; i++ )
        {
            assertEquals( strings[strings.length-i-1], trie.findEqual( strings[i] ).pollFirst() );
        }
    }

    @Test
    public void testDelete()
    {
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>();

        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }

        for ( int i = 0; i < strings.length; i++ )
        {
            assertEquals( strings[i], trie.deleteEqual( strings[i] ).pollFirst() );
            assertEquals( null, trie.findEqual( strings[i] ) );
        }

        for ( int i = 0; i < strings.length; i++ )
        {
            assertEquals( null, trie.findEqual( strings[i] ) );
        }

        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }

        for ( int i = 0; i < strings.length; i++ )
        {
            assertEquals( strings[i], trie.findEqual( strings[i] ).pollFirst() );
        }
    }

    @Test
    public void testFindInsertDelete()
    {
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>();
        final int half = strings.length / 2;
        final int whole = half * 2;

        for ( int i = 0; i < half; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }

        for ( int i = 0; i < half; i++ )
        {
            assertEquals( strings[i], trie.deleteEqual( strings[i] ).pollFirst() );
            assertEquals( null, trie.findEqual( strings[i] ) );

            trie.insert( strings[i + half], strings[i + half] );
        }

        for ( int i = 0; i < half; i++ )
        {
            assertEquals( null, trie.findEqual( strings[i] ) );
            assertEquals( null, trie.deleteEqual( strings[i] ) );
        }

        for ( int i = half; i < whole; i++ )
        {
            assertEquals( strings[i], trie.deleteEqual( strings[i] ).pollFirst() );
        }
    }
}
