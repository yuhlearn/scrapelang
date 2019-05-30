package com.yuhlearn.scrapelang;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;

import com.yuhlearn.datastructures.TriGram;
import com.zaxxer.sparsebits.SparseBitSet;

public class TriExtract 
{
    public static void main( String[] args )
    {
        String[] strings = new String[100000];
        String path = "src/test/java/com/yuhlearn/datastructures/strings.txt";
        int noCharacters = 0;
        int minimumString = 100000000;
        int maximumString = -1;

        try
        {
            BufferedReader reader = new BufferedReader( new FileReader( path ) );

            for ( int i = 0; i < strings.length; i++ )
            {
                strings[i] = reader.readLine();
                noCharacters += strings[i].length();
                minimumString = Math.min( minimumString, strings[i].length() );
                maximumString = Math.max( maximumString, strings[i].length() );
            }

            reader.close();
        }
        catch ( Exception e )
        {
            System.out.println( e );
        }

        // TEST GROUND
        TriGram triGram = new TriGram();

        final long insertStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            triGram.insert( strings[i], i );
        }
        final double insertTime = ( System.currentTimeMillis() - insertStart ) * 0.001;

        final long findStart = System.currentTimeMillis();
        for ( int i = 0; i < strings.length; i++ )
        {
            SparseBitSet tmp = triGram.find( strings[i] );
        }
        final double findTime = ( System.currentTimeMillis() - findStart ) * 0.001;

        Set<Integer> keys = triGram.keys();

        int noEntries = keys.size();
        int noValues = 0;
        int minimumEntry = 100000000;
        int maximumEntry = -1;
        Iterator<Integer> iterator = keys.iterator();

        while ( iterator.hasNext() )
        {
            int entryLength = triGram.get( iterator.next() ).size();
            minimumEntry = Math.min( minimumEntry, entryLength );
            maximumEntry = Math.max( maximumEntry, entryLength );
            noValues += entryLength;
        }

        out.println( "Number of entries: " + noEntries );
        out.println( "Number of values: " + noValues );
        out.println();
        out.println( "Minimum entry length: " + minimumEntry );
        out.println( "Maximum entry length: " + maximumEntry );
        out.println( "Average entry length: " + ( noValues / noEntries ) );
        out.println();
        out.println( "Minimum string length: " + minimumString );
        out.println( "Maximum string length: " + maximumString );
        out.println( "Average string length: " + ( noCharacters / strings.length ) );
        out.println();
        out.println( triGram.className() + " times:" );
        out.println( "\tInsert " + insertTime + " s" );
        out.println( "\tFind " + findTime + " s" );
    }
}
