package com.yuhlearn.datastructures;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.openjdk.jmh.annotations.*;

public class IndexFindBenchmark
{
    @State(Scope.Benchmark)
    public static class BMState 
    {
        public String[] strings = new String[100000];

        public DoubleArrayTrie<Integer> trie = new DoubleArrayTrie<Integer>();
        public TriGramIndex trigram = new TriGramIndex();
        public HashMap<String, Integer> hashmap = new HashMap<String, Integer>();

        private String path = "/strings.txt";

        @Param( {"1000", "10000", "100000"} )
        public int iterations;

        @Setup(Level.Trial)
        public void doSetup() throws Exception
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

            for ( int i = 0; i < strings.length; i++ )
            {
                trie.insert( strings[i], i );
                trigram.insert( strings[i], i );
                hashmap.put( strings[i], i );
            }
        }
    }

    @Benchmark
    public void findDoubleArrayTrie( BMState state ) throws Exception 
    {
        for ( int i = 0; i < state.iterations; i++ )
        {
            state.trie.findEqual( state.strings[i] );
        }
    }

    @Benchmark
    public void findTriGram( BMState state ) throws Exception 
    {
        for ( int i = 0; i < state.iterations; i++ )
        {
            state.trigram.find( state.strings[i] );
        }
    }

    @Benchmark
    public void findHashMap( BMState state ) throws Exception 
    {
        for ( int i = 0; i < state.iterations; i++ )
        {
            state.hashmap.get( state.strings[i] );
        }
    }
}