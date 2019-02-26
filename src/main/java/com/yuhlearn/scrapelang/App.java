package com.yuhlearn.scrapelang;

import java.io.BufferedReader;
import java.io.FileReader;

import com.yuhlearn.datastructures.DoubleArrayTrie;

public class App 
{
    public static void main( String[] args )
    {
        String[] strings = new String[1000000];
        String path = "src/test/java/com/yuhlearn/datastructures/strings.txt";

        try
        {
            BufferedReader reader = new BufferedReader( new FileReader( path ) );

            for ( int i = 0; i < strings.length; i++ )
                strings[i] = reader.readLine();

            reader.close();
        }
        catch ( Exception e )
        {
            System.out.println( e );
        }

        DoubleArrayTrie<String> trie = new DoubleArrayTrie<String>();

        // Insert all strings
        final long insertStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }
        final long insertEnd = System.currentTimeMillis();

        // Find all strings
        final long findStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.findEqual( strings[i] );
        }
        final long findEnd = System.currentTimeMillis();

        // Delete all strings
        final long deleteStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.deleteEqual( strings[i] );
        }
        final long deleteEnd = System.currentTimeMillis();

        // Delete half of the entries and insert them again
        final int half = strings.length / 2;

        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }

        final long deleteInsertStart = System.currentTimeMillis();
        for ( int i = 0; i < half; i++ )
        {
            trie.deleteEqual( strings[i] );
        }
        for ( int i = 0; i < half; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }
        final long deleteInsertEnd = System.currentTimeMillis();

        final long insertTime = (insertEnd - insertStart);
        final long deleteTime = (deleteEnd - deleteStart);
        final long findTime = (findEnd - findStart);
        final long deleteInsertTime = (deleteInsertEnd - deleteInsertStart);

        System.out.println("Insert execution time: " + insertTime + " ms");
        System.out.println("Find   execution time: " + findTime + " ms");
        System.out.println("Delete execution time: " + deleteTime + " ms");
        System.out.println("Delins execution time: " + deleteInsertTime + " ms");
    }
}
