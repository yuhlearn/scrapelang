package com.yuhlearn.scrapelang;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class RegexTest
{
    public static void main( String[] args )
    {
        RegExp r = new RegExp( "ab(c|de)*(gah){0,5}" );
        Automaton a = r.toAutomaton();
        String s = "abcccdc";
        System.out.println( r );
    }
}
