package com.yuhlearn.scrapelang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.TreeMap;

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
        HashMap<String,String> hash = new HashMap<String,String>();
        TreeMap<String,String> tree = new TreeMap<String,String>();

        // Insert all strings
        final long insertStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.insert( strings[i], strings[i] );
        }
        final long insertEnd = System.currentTimeMillis();

        final long hashInsertStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            hash.put( strings[i], strings[i] );
        }
        final long hashInsertEnd = System.currentTimeMillis();

        final long treeInsertStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            tree.put( strings[i], strings[i] );
        }
        final long treeInsertEnd = System.currentTimeMillis();


        // Find all strings
        final long findStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.findEqual( strings[i] );
        }
        final long findEnd = System.currentTimeMillis();

        final long hashFindStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            hash.get( strings[i] );
        }
        final long hashFindEnd = System.currentTimeMillis();

        final long treeFindStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            tree.get( strings[i] );
        }
        final long treeFindEnd = System.currentTimeMillis();


        // Delete all strings
        final long deleteStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            trie.deleteEqual( strings[i] );
        }
        final long deleteEnd = System.currentTimeMillis();

        final long hashDeleteStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            hash.remove( strings[i] );
        }
        final long hashDeleteEnd = System.currentTimeMillis();

        final long treeDeleteStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            tree.remove( strings[i] );
        }
        final long treeDeleteEnd = System.currentTimeMillis();


        final long insertTime = (insertEnd - insertStart);
        final long hashInsertTime = (hashInsertEnd - hashInsertStart);
        final long treeInsertTime = (treeInsertEnd - treeInsertStart);

        final long findTime = (findEnd - findStart);
        final long hashFindTime = (hashFindEnd - hashFindStart);
        final long treeFindTime = (treeFindEnd - treeFindStart);

        final long deleteTime = (deleteEnd - deleteStart);
        final long hashDeleteTime = (hashDeleteEnd - hashDeleteStart);
        final long treeDeleteTime = (treeDeleteEnd - treeDeleteStart);


        System.out.print("Insert execution time");
        System.out.print(":\ttrie " + insertTime + " ms");
        System.out.print(",\thash " + hashInsertTime + " ms");
        System.out.print(",\ttree " + treeInsertTime + " ms");
        System.out.println();

        System.out.print("Lookup execution time");
        System.out.print(":\ttrie " + findTime + " ms");
        System.out.print(",\thash " + hashFindTime + " ms");
        System.out.print(",\ttree " + treeFindTime + " ms");
        System.out.println();

        System.out.print("Delete execution time");
        System.out.print(":\ttrie " + deleteTime + " ms");
        System.out.print(",\thash " + hashDeleteTime + " ms");
        System.out.print(",\ttree " + treeDeleteTime + " ms");
        System.out.println();
    }
}
