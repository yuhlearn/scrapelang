package com.yuhlearn.datastructures;

import java.util.Arrays;

public class SortedArraySet
{
    private int[] set;
    private int lastValue;
    private int next;
    private boolean isSet;

    public SortedArraySet()
    {
        this( 64 );
    }

    private SortedArraySet( int size )
    {
        set = new int[size];
        lastValue = -1;
        next = 0;
        isSet = true;
    }

    public void add( int value )
    {
        if ( next >= set.length )
            set = Arrays.copyOf( set, set.length << 1 );
        
        if ( value != lastValue )
        {           
            if ( value < lastValue )
                isSet = false;
            set[next++] = value;
            lastValue = value;
        }
    }

    public void intersect( SortedArraySet other )
    {
        int newNext = 0;
        int otherNext = other.next;
        int[] otherSet = other.set;

        // Both arrays must be sorted sets before we can proceed
        other.ensureSet();
        this.ensureSet();

        for ( int i = 0, j = 0; i < next && j < otherNext; i++ )
        {
            int current = set[i];

            // Find the next element in the other set that is not
            // smaller than the current element
            while ( j < otherNext && current > otherSet[j] )
                j++;

            // If the next element in the other set is equal to current
            // (otherwise current must be smaller and has to be incremented)
            if ( j < otherNext && current == otherSet[j] )
                set[newNext++] = current;
        }

        lastValue = ( newNext > 0 ) ? set[newNext - 1] : -1;
        next = newNext;
    }

    public void union( SortedArraySet other )
    {
        int[] newSet;
        int[] otherSet = other.set;
        int newNext = 0;
        int otherNext = other.next;
        
        other.ensureSet();
        this.ensureSet();

        newSet = new int[64 << ( ( 32 - noz( ( next + other.next ) >> 6 ) ) )];

        int i = 0, j = 0;

        while ( i < next && j < otherNext ) 
        { 
            if ( set[i] < otherSet[j] )
                newSet[newNext++] = set[i++];
            else if ( otherSet[j] < set[i]) 
                newSet[newNext++] = otherSet[j++];
            else
            {
                newSet[newNext++] = set[i++];
                j++; 
            } 
        } 

        while ( i < next ) 
            newSet[newNext++] = set[i++];
        while ( j < otherNext ) 
            newSet[newNext++] = otherSet[j++];
           
        set = newSet;
        lastValue = ( newNext > 0 ) ? newSet[newNext - 1] : -1;
        next = newNext;
    }

    private int noz( int i )
    {
        return Integer.numberOfLeadingZeros( i );
    }

    private void ensureSet()
    {
        if ( isSet == false )
        {
            int limit = next;
            lastValue = -1;
            next = 0;
            isSet = true;

            Arrays.sort( set, 0, limit );

            for ( int i = 0; i < limit; i++ )
                if ( set[i] != lastValue )
                    lastValue = set[next++] = set[i];
        }
    }

    public SortedArraySet clone()
    {
        SortedArraySet other = new SortedArraySet( this.set.length );
        
        for ( int i = 0; i < this.next; i++ )
            other.set[i] = this.set[i];
        
        other.lastValue = this.lastValue;
        other.next = this.next;
        other.isSet = this.isSet;

        return other;
    }

    public void copy( SortedArraySet other )
    {
        set = Arrays.copyOf( other.set, other.set.length );
        lastValue = other.lastValue;
        next = other.next;
        isSet = other.isSet;
    }

    public boolean isEmpty()
    {
        return next == 0;
    }

    public int[] toArray()
    {
        this.ensureSet();
        return Arrays.copyOf( set, next );
    }
}