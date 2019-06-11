package com.yuhlearn.datastructures;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.openjdk.jmh.annotations.*;

public class IndexInsertBenchmark
{
    @State( Scope.Benchmark )
    public static class BMState 
    {
        public String[] strings = new String[1000000];
        private String path = "/strings.txt";

        @Param( {"1000", "10000", "100000"} )
        public int iterations;

        @Setup( Level.Trial )
        public void doSetup() 
        {
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
    }

    @Benchmark
    public void insertDoubleArrayTrie( BMState state ) throws Exception 
    {
        DoubleArrayTrie<Integer> trie = new DoubleArrayTrie<Integer>();
        
        for ( int i = 0; i < state.iterations; i++ )
        {
            trie.insert( state.strings[i], i );
        }
    }

    @Benchmark
    public void insertTriGram( BMState state ) throws Exception 
    {
        TriGramIndex trigram = new TriGramIndex();

        for ( int i = 0; i < state.iterations; i++ )
        {
            trigram.insert( state.strings[i], i );
        }
    }

    @Benchmark
    public void insertHashmap( BMState state ) throws Exception 
    {
        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();

        for ( int i = 0; i < state.iterations; i++ )
        {
            hashmap.put( state.strings[i], i );
        }
    }
}