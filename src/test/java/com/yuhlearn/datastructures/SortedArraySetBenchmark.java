package com.yuhlearn.datastructures;

import java.util.Random;

import org.openjdk.jmh.annotations.*;

public class SortedArraySetBenchmark
{
    @State(Scope.Benchmark)
    public static class BMState 
    {
        int arrayLength = 1000000;
        int[] numbers;
        Random rand = new Random();

        SortedArraySet setA;
        SortedArraySet setB;

        @Param( {"1000", "10000", "100000", "1000000"} )
        public int iterations;

        @Setup(Level.Iteration)
        public void doSetup() throws Exception
        {
            numbers = new int[arrayLength];

            setA = new SortedArraySet();
            setB = new SortedArraySet();

            for ( int i = 0; i < arrayLength; i++ )
                numbers[i] = rand.nextInt( arrayLength );
            
            for ( int i = 0; i < iterations; i++ )
            {
                setA.add( numbers[i] );
                setB.add( numbers[arrayLength - i - 1] );
            }            
        }
    }

    @Benchmark
    public void addSortedArraySet( BMState state ) throws Exception 
    {
        SortedArraySet set = new SortedArraySet();

        for ( int i = 0; i < state.iterations; i++ )
        {
            set.add( state.numbers[i] );
        }
    }

    @Benchmark
    public void toArraySortedArraySet( BMState state ) throws Exception 
    {
        SortedArraySet set = state.setA;
        set.toArray();
    }

    @Benchmark
    public void intersectSortedArraySet( BMState state ) throws Exception 
    {
        SortedArraySet setA = state.setA;
        SortedArraySet setB = state.setB;

        setA.intersect( setB );
    }

    @Benchmark
    public void unionSortedArraySet( BMState state ) throws Exception 
    {
        SortedArraySet setA = state.setA;
        SortedArraySet setB = state.setB;

        setA.union( setB );
    }
}