package com.yuhlearn.datastructures;

import java.util.Arrays;

public class TriNode implements Comparable<TriNode>
{
    private final int chars;

    public TriNode( final int chars )
    {
        if ( chars < 0 )
            throw new IllegalArgumentException( "chars = " + chars );
        
        this.chars = chars;
    }

    @Override 
    public boolean equals( Object otherObject ) {
        if (this == otherObject)
            return true;

        if ( otherObject == null || getClass() != otherObject.getClass() )
            return false;

        TriNode otherNode = (TriNode) otherObject;
        return chars == otherNode.chars;
    } 

    @Override 
    public int hashCode() {
        return Integer.hashCode( chars );
    }

    @Override 
    public String toString() { 
        return Integer.toString( chars ); 
    }

    public int compareTo( TriNode otherObject ) {
        return Integer.compare( chars, otherObject.chars );
    }
}