package com.yuhlearn.datastructures;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for the SortedArraySet class.
 */
@RunWith(JUnit4.class)
public class SortedArraySetTest 
{
    final int arrayLength = 100000;
    int[] sorted = new int[arrayLength];
    int[] random = new int[arrayLength];
    Random rand = new Random();

    @Before
    public void init()
    {    
        for ( int i = 0; i < arrayLength; i++ )
        {
            sorted[i] = i;
            random[i] = rand.nextInt( arrayLength / 10 );
        }
    }

    @Test
    public void testInsertOrdered()
    {
        SortedArraySet set = new SortedArraySet();

        for ( int i = 0; i < arrayLength; i++ )
            set.add( sorted[i] );
        
        int[] setArray = set.toArray();

        assertEquals( arrayLength, setArray.length );

        for ( int i = 0; i < arrayLength; i++ )
            assertEquals( sorted[i], setArray[i] );
    }

    @Test
    public void testInsertReversed()
    {
        SortedArraySet set = new SortedArraySet();

        for ( int i = 0; i < arrayLength; i++ )
            set.add( sorted[arrayLength - i - 1] );
        
        int[] setArray = set.toArray();

        assertEquals( arrayLength, setArray.length );

        for ( int i = 0; i < arrayLength; i++ )
            assertEquals( sorted[i], setArray[i] );
    }

    @Test
    public void testInsertRandom()
    {
        SortedArraySet set = new SortedArraySet();

        for ( int i = 0; i < arrayLength; i++ )
            set.add( random[arrayLength - i - 1] );
        
        int[] setArray = set.toArray();

        assertTrue( arrayLength >= setArray.length );

        for ( int i = 0; i < arrayLength; i++ )
            assertTrue( Arrays.binarySearch( setArray, random[i] ) >= 0 );
    }

    @Test
    public void testIntersection()
    {
        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();
        SortedArraySet setC = new SortedArraySet();

        int start = arrayLength / 4; 
        int end = ( arrayLength * 3 ) / 4;

        for ( int i = 0; i < end; i++ )
            setA.add( sorted[i] );

        for ( int i = start; i < arrayLength; i++ )
            setB.add( sorted[i] );

        setC.copy( setA );
        setA.intersect( setB );
        setB.intersect( setC );

        int[] setArrayA = setA.toArray();
        int[] setArrayB = setB.toArray();

        assertEquals( end - start, setArrayA.length );
        assertEquals( end - start, setArrayB.length );
        assertEquals( setArrayA.length, setArrayB.length );
        
        for ( int i = 0; i < setArrayA.length; i++ )
            assertEquals( setArrayA[i], setArrayB[i]);

        for ( int i = 0; i < setArrayA.length; i++ )
            assertEquals( setArrayA[i], sorted[i + start]);

        for ( int i = 0; i < setArrayB.length; i++ )
            assertEquals( setArrayB[i], sorted[i + start]);
    }

    @Test
    public void testIntersection1()
    {
        int[] inputA = {};
        int[] inputB = {};
        int[] expected = {};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.intersect( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testIntersection2()
    {
        int[] inputA = {6,4,23,2};
        int[] inputB = {};
        int[] expected = {};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.intersect( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testIntersection3()
    {
        int[] inputA = {0};
        int[] inputB = {0};
        int[] expected = {0};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.intersect( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testIntersection4()
    {
        int[] inputA = {5,3,8,2,5};
        int[] inputB = {6,2,6,2,3};
        int[] expected = {2,3};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.intersect( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testIntersection5()
    {
        int[] inputA = {6,7,8,9};
        int[] inputB = {5,4,3,2,1};
        int[] expected = {};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.intersect( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testUnion()
    {
        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();
        SortedArraySet setC = new SortedArraySet();

        int start = arrayLength / 4; 
        int end = ( arrayLength * 3 ) / 4;

        for ( int i = 0; i < end; i++ )
            setA.add( sorted[i] );

        for ( int i = start; i < arrayLength; i++ )
            setB.add( sorted[i] );

        setC.copy( setA );
        setA.union( setB );
        setB.union( setC );

        int[] setArrayA = setA.toArray();
        int[] setArrayB = setB.toArray();

        assertEquals( arrayLength, setArrayA.length );
        assertEquals( arrayLength, setArrayB.length );
        assertEquals( setArrayA.length, setArrayB.length );
        
        for ( int i = 0; i < setArrayA.length; i++ )
            assertEquals( setArrayA[i], setArrayB[i]);

        for ( int i = 0; i < setArrayA.length; i++ )
            assertEquals( setArrayA[i], sorted[i]);

        for ( int i = 0; i < setArrayB.length; i++ )
            assertEquals( setArrayB[i], sorted[i]);
    }

    @Test
    public void testUnion1()
    {
        int[] inputA = {};
        int[] inputB = {};
        int[] expected = {};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.union( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testUnion2()
    {
        int[] inputA = {8,4,7,2,3,7,4};
        int[] inputB = {};
        int[] expected = {2,3,4,7,8};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.union( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testUnion3()
    {
        int[] inputA = {8,4,7,2,3,7,4};
        int[] inputB = {9,7,2,4,8};
        int[] expected = {2,3,4,7,8,9};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.union( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testUnion4()
    {
        int[] inputA = {0,0,0,0,0};
        int[] inputB = {0,0,0,0};
        int[] expected = {0};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.union( setB );

        assertArrayEquals( expected, setA.toArray() );
    }

    @Test
    public void testUnion5()
    {
        int[] inputA = {};
        int[] inputB = {909840983,0};
        int[] expected = {0,909840983};

        SortedArraySet setA = new SortedArraySet();
        SortedArraySet setB = new SortedArraySet();

        for ( int element : inputA ) setA.add( element );
        for ( int element : inputB ) setB.add( element );

        setA.union( setB );

        assertArrayEquals( expected, setA.toArray() );
    }
}