package com.yuhlearn.datastructures;

import java.util.Arrays;

public class DoubleArrayTrie<E> 
{
    private static final int START_STATE = 1;
    private static final int FIRST_LINK  = 0;
    private static final int LEAF_STATE = -1;
    private static final int INIT_LAST_LINK = 2;

    private int[] base;
    private int[] check;
    private TailData<E>[] tail;

    private int capacity;
    private int minCapacity;
    private int maxState;

    public DoubleArrayTrie()
    {
        this( 1024 );
    }

    public DoubleArrayTrie( int initialCapacity ) 
    {
        capacity = initialCapacity;
        minCapacity = initialCapacity;
        maxState = START_STATE;

        base = new int[capacity];
        check = new int[capacity];
        tail = (TailData<E>[]) new TailData[capacity];

        // The base of nodes adjacent to the start state start at INIT_LAST_LINK
        base[START_STATE] = INIT_LAST_LINK;

        // The first link in the G-link points BACK to the last free link.
        base[FIRST_LINK] = -INIT_LAST_LINK;
        // The last free link in the G-link points BACK to the first link.
        base[INIT_LAST_LINK] = -FIRST_LINK;

        // The first free link in the G-link points FORWARD to the last free link.
        check[FIRST_LINK] = -INIT_LAST_LINK;
        // The last free link in the G-link points FORWARD to the last free link.
        check[INIT_LAST_LINK] = -FIRST_LINK; 
    }

    // Return the index of the last link
    private int getLastLink() 
    {
        return -base[FIRST_LINK];
    }

    // Return the index of the second link
    private int getSecondLink()
    {
        return -check[FIRST_LINK];
    }

    // Return the index of the next link after link
    private int getNextLink( int link )
    {
        return -check[link];
    }

    // Return the index of the previous link after link
    private int getPreviousLink( int link )
    {
        return -base[link];
    } 

    private int getNextState( final int state, final byte[] string, final int index )
    {
        int character = UTF8CharMap.byteToInt( string[index] );
        int nextState = base[state] + character;
 
        return nextState;
    }

    public ResultSet<E> findEqual( final String string )
    {
        return findEqual( string.getBytes( UTF8CharMap.CHARSET ) );
    }

    // Return a data object if string exists in trie, otherwise null.
    public ResultSet<E> findEqual( final byte[] string )
    {
        final int state = findEqualAux( string );

        if ( state > -1 )
            return tail[state].getData();
        
        return null;
    }

    // Return the state number associated with string if it exists in trie, otherwise -1
    private int findEqualAux( final byte[] string )
    {
        final int stringLength = string.length;
        final int lastLink = getLastLink();
        int state = START_STATE;
        int index = 0;

        while ( ( index < stringLength ) && ( base[state] != LEAF_STATE ) )
        {
            int nextState = getNextState( state, string, index );

            // We reached unknown territory, but we did not exhaust the string
            if ( ( nextState >= lastLink ) || ( check[nextState] != state ) )
                return -1;

            index++;
            state = nextState;
        }

        // We exhausted the string or reached a leaf, so result must be stored in state
        TailData<E> tailData = tail[state];

        if ( tailData != null )
        {
            byte[] suffixA = Arrays.copyOfRange( string, index, string.length );
            byte[] suffixB = tailData.getSuffix();
            int count = stringCompare( suffixA, suffixB );

            if ( count == -1 )
                return state;
        }

        return -1;
    }
    
    public ResultSet<E> deleteEqual( final String string )
    {
        return deleteEqual( string.getBytes( UTF8CharMap.CHARSET ) );
    }

    public ResultSet<E> deleteEqual( final byte[] string )
    {
        final int deleteState = findEqualAux( string );
        ResultSet<E> returnData = null;

        if ( deleteState > -1 )
        {
            returnData = tail[deleteState].getData();
            int current = deleteState;

            // IF the current state is not the start state AND it is a leaf state 
            if ( ( current != START_STATE ) && ( base[current] == LEAF_STATE ) )
            {
                do 
                {
                    // Delete the state and tail data and walk the tree backwards
                    int parent = check[current];
                    makeLink( current, getLinkAfter( current ) );
                    current = parent;
                }
                while ( ( current != START_STATE ) && ( tail[current] == null ) && ! hasTransitions( current ) );
                
                // We have to mark the current state as leaf if it is one
                if ( ( current != START_STATE ) && ! hasTransitions( current ) )
                    base[current] = LEAF_STATE;
            }
            else
            {
                // We are not allowed to delete the start state or states that are not leaves
                tail[current] = null;
            }

            tryShrinkCapacity( deleteState );            
        }
    
        return returnData;
    }

    private boolean hasTransitions( final int state )
    {
        final int stateBase = base[state];

        for ( int c = 0; c <= UTF8CharMap.MAX_UTF8_VALUE; c++ )
            if ( check[stateBase + c] == state )
                return true;

        return false;
    }

    public void insert( final String string, final E data ) 
    {
        insert( string.getBytes( UTF8CharMap.CHARSET ) , data );
    }

    public void insert( final byte[] string, final E data ) 
    {
        final int stringLength = string.length;
        final int lastLink = getLastLink();
        int state = START_STATE;
        int index = 0;

        while ( index < stringLength )
        {
            int nextState = getNextState( state, string, index );

            // We reached unknown territory OR the next state is taken
            if ( ( nextState >= lastLink ) || ( check[nextState] != state ) )
            {
                // Insert a new branch starting from the previous state
                insertNewBranch( string, index, state, data );
                return;
            }

            // Increment the index early so it points to the next character,
            // because this is what will be stored in tail
            ++index;

            // We reached a leaf state
            if ( base[nextState] == LEAF_STATE )
            {
                byte[] suffixA = Arrays.copyOfRange( string, index, string.length );
                byte[] suffixB = tail[nextState].getSuffix();

                // The length of the common prefix
                int count = stringCompare( suffixA, suffixB );

                // IF the strings are not equal
                if ( count != -1 )
                {                   
                    // The common prefix 
                    byte[] prefix = Arrays.copyOfRange( suffixA, 0, count );

                    // The non-matching suffixes
                    suffixA = Arrays.copyOfRange( suffixA, count, suffixA.length );
                    suffixB = Arrays.copyOfRange( suffixB, count, suffixB.length );

                    insertUnfoldTail( prefix, suffixA, suffixB, nextState, data );
                }
                else
                {
                    // The strings are equal
                    tail[nextState].insert( data );
                }

                return;
            }
            
            state = nextState;
        }

        // We exhaused the string and state is not a leaf, so we just insert where we are
        TailData<E> tailData = tail[state];

        if ( tailData == null )
            tail[state] = new TailData<E>( new byte[0], data );
        else
            tail[state].insert( data );

        return;
    }

    private void insertUnfoldTail( final byte[] prefix, final byte[] suffixA, final byte[] suffixB, final int sourceState, final E data )
    {
        int state = insertPrefix( prefix, sourceState );

        ResultSet<E> oldData = tail[sourceState].getData();
        tail[sourceState] = null;

        if ( suffixA.length == 0 )
        {
            base[state] = findBase( UTF8CharMap.byteToInt( suffixB[0] ) );

            tail[state] = new TailData<E>( new byte[0], data );
            insertNewLeaf( state, suffixB, 0, oldData );            
        } 
        else if ( suffixB.length == 0 )
        {
            base[state] = findBase( UTF8CharMap.byteToInt( suffixA[0] ) );

            insertNewLeaf( state, suffixA, 0, data );
            tail[state] = new TailData<E>( new byte[0], oldData );
        }
        else
        {
            // Find a base that can branch both characters
            int[] characters = 
                { UTF8CharMap.byteToInt( suffixA[0] ), 
                  UTF8CharMap.byteToInt( suffixB[0] ) };
            Arrays.sort( characters );
            
            int newBase = findBase( characters );
            base[state] = newBase;

            insertNewLeaf( state, suffixA, 0, data );
            insertNewLeaf( state, suffixB, 0, oldData );
        }
    }

    private int insertPrefix( final byte[] prefix, final int sourceState )
    {
        int state = sourceState;

        // Extend the trie with states for the common prefix
        for ( byte c : prefix )
        {
            int character = UTF8CharMap.byteToInt( c );
            int nextBase = findBase( character );
            int nextState = nextBase + character;

            ensureCapacity( nextState );
            breakLink( nextState );

            // Give the current state a new base and make the new state refer back to it 
            base[state] = nextBase;
            check[nextState] = state;

            state = nextState;
        }

        return state;
    }

    private void insertNewBranch( final byte[] string, final int index, final int state, final E data ) 
    {
        int nextState = getNextState( state, string, index );

        ensureCapacity( nextState );

        // IF the next state is taken
        if ( check[nextState] >= START_STATE )
        {
            // Move the base of state OR the base of the parent of the obstructing state
            final int currentParent = check[nextState];
            final int character = UTF8CharMap.byteToInt( string[index] );
            
            final int[] charactersA = transitionArray( state, character );

            if ( check[state] != currentParent )
            {
                final int[] charactersB = transitionArray( currentParent, -1 );

                if ( charactersA.length > charactersB.length )
                    moveBase( currentParent, charactersB, -1 );
                else
                    moveBase( state, charactersA, character );
            }
            else
            {
                moveBase( state, charactersA, character );
            }
        }

        insertNewLeaf( state, string, index, data );
    }

    // Move base for states adjacent to state to give room for character
    private void moveBase( final int state, final int[] characters, final int character )
    {
        final int oldBase = base[state];
        final int newBase = findBase( characters );
        int nextLink = getSecondLink();

        // Set the new base for the source state
        base[state] = newBase;
        
        // Move the transitions
        for ( int c : characters )
        {
            // IF c is not the character we are trying to insert
            if ( c != character )
            {
                int oldState = oldBase + c;
                int newState = newBase + c;

                // make the transitions from oldState refer back to newState
                for ( int d : transitionArray(oldState, -1) )
                    check[base[oldState] + d] = newState;

                if ( nextLink == newState )
                    nextLink = getNextLink( nextLink );

                breakLink( newState );
                copyState( oldState, newState );
                nextLink = getLinkAfter( nextLink, oldState );                
                makeLink( oldState, nextLink );
            }
        }
    }

    // Copy data from a state to another
    private void copyState( final int fromState, final int toState )
    {
        check[toState] = check[fromState];
        base[toState] = base[fromState];
        tail[toState] = tail[fromState];
    }

    // Remove the link
    private void breakLink( final int link )
    {
        int nextLink = getNextLink( link );
        int previousLink = getPreviousLink( link );

        check[previousLink] = -nextLink;
        base[nextLink] = -previousLink;

        if ( link > maxState )
            maxState = link;
    }

    // Make state a link before nextLink
    private void makeLink( final int state, final int nextLink )
    {
        int previousLink = getPreviousLink( nextLink );

        check[state] = -nextLink; 
        check[previousLink] = -state;

        base[state] = -previousLink; 
        base[nextLink] = -state;

        tail[state] = null;
    }

    private int getLinkAfter( final int state )
    {
        int index = state;

        while ( check[index] > FIRST_LINK )
            index++;

        return index;
    }

    // Get the first link after state, starting from initialLink
    private int getLinkAfter( final int initialLink, final int state )
    {
        int nextLink = initialLink;

        ensureCapacity( state + 1 );

        // Find the first link after state
        while ( nextLink <= state )
            nextLink = getNextLink( nextLink );

        return nextLink;
    }

    private int findBase( final int character )
    {
        int nextLink = getLinkAfter( FIRST_LINK, INIT_LAST_LINK + character );

        return nextLink - character;
    }

    // Find a new base where characters can be inserted.
    // Assumes that characters is sorted.
    private int findBase( final int[] characters )
    {
        final int setLength = characters.length;
        final int max = characters[setLength - 1];
        final int firstCharacter = characters[0];

        int nextLink = getLinkAfter( FIRST_LINK, INIT_LAST_LINK + firstCharacter );

        while ( nextLink > 0 )
        {
            int count = 1;
            int newBase = nextLink - firstCharacter;

            ensureCapacity( newBase + max );

            while ( ( count < setLength ) && ( check[newBase + characters[count]] < 0 ) )
                count++;

            // IF all characters fit with newBase
            if ( count == setLength )
                return newBase;

            nextLink = getNextLink( nextLink );
        }

        throw new Error( "Error traversing the G-list." );
    }

    // Return a sorted array containing all characters reachable from state
    private int[] transitionArray( final int state, final int character )
    {
        int[] characters;
        final int stateBase = base[state];
        final int lastLink = getLastLink();
        int count = ( character == -1 ) ? 0 : 1; // Is one reserved for character?

        // Leaves don't have transitions
        if ( stateBase == LEAF_STATE )
            return new int[0];

        // Count the number of transitions from state
        for ( int c = 0; ( c <= UTF8CharMap.MAX_UTF8_VALUE ) && ( stateBase + c < lastLink ); c++ )
            if ( check[stateBase + c] == state )
                count++;

        characters = new int[count];

        // Insert the transitions into the characters array
        for ( int c = 0, i = 0; i < count; c++ )
            if ( ( check[stateBase + c] == state ) || ( c == character ) )
                characters[i++] = c;

        // Return a sorted array of characters
        return characters;
    }

    // Insert the character string[index] into the double array
    // and the substring string[index+1:] into trail
    private void insertNewLeaf( final int state, final byte[] string, final int index, final E data )
    {
        ResultSet<E> dataSet = new ResultSet<E>();
        dataSet.add( data );
        insertNewLeaf(state, string, index, dataSet );
    }

    private void insertNewLeaf( final int state, final byte[] string, final int index, final ResultSet<E> data )
    {
        int nextState = getNextState( state, string, index );

        ensureCapacity( nextState );

        // Remove the new state from the the G-link
        breakLink( nextState );

        // Make the new state point back to the source state and mark as leaf
        check[nextState] = state;
        base[nextState] = LEAF_STATE;

        // Insert the new data
        tail[nextState] = new TailData<E>( Arrays.copyOfRange( string, index + 1, string.length ), data );
    }

    // Return -1 if stringA is equal to stringB, 
    // otherwise return the length of the longest prefix
    private int stringCompare( final byte[] stringA, final byte[] stringB )
    {
        int lengthA = stringA.length;
        int lengthB = stringB.length;
        int minLength = Math.min( lengthA, lengthB );
        
        for ( int index = 0; index < minLength; index++ )
        {
            if ( stringA[index] != stringB[index] )
                return index;
        }

        return ( lengthA == lengthB ) ? -1 : minLength ;
    }

    private void ensureCapacity( final int state )
    {
        // IF there is not room for the state plus a last link
        if ( state + 2 > capacity ) 
        {            
            capacity = Math.max( state + 2, capacity * 2);

            base = Arrays.copyOf( base, capacity );
            check = Arrays.copyOf( check, capacity );
            tail = Arrays.copyOf( tail, capacity );
        }

        // The last link must be after the last state
        extendGLink( state + 1 );
    }

    // Starting from the currently last link, extend the G-link to a new last link
    private void extendGLink( final int newLastLink )
    {
        int link = getLastLink();
        
        if ( newLastLink > link )
        {
            // base[link] already points to the previous link,
            // make check[link] point to the next link
            check[link] = - ( link + 1 );

            // Iterate over the links inbetween the previously last link and the new one
            for ( int nextLink = link + 1; nextLink < newLastLink; nextLink++ ) 
            {
                check[nextLink] = - ( nextLink + 1 );
                base[nextLink] = - ( nextLink - 1 );
            }

            // Set the new last link
            check[newLastLink] = - FIRST_LINK;
            base[newLastLink] = - ( newLastLink - 1 );
            base[FIRST_LINK] = - newLastLink;
        }
    }

    // Try to shrink the trie, assuming that state was just deleted
    private void tryShrinkCapacity( final int state )
    {
        if ( base[maxState] < LEAF_STATE )
        {
            updateMaxState();

            if ( maxState <= ( capacity / 4 ) && minCapacity <= ( capacity / 2 ) ) 
            {
                capacity = capacity / 2;

                final int newLastLink = Math.min( getLastLink(), capacity - 1 );
                
                base = Arrays.copyOf( base, capacity );
                check = Arrays.copyOf( check, capacity );
                tail = Arrays.copyOf( tail, capacity );

                base[FIRST_LINK] = - newLastLink;
                check[newLastLink] = - FIRST_LINK;            
            }
        }
    }

    private void updateMaxState()
    {
        int newMaxState = maxState;

        while ( base[newMaxState] < LEAF_STATE )
            --newMaxState;

        maxState = newMaxState;
    }
}
