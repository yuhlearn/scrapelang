package com.yuhlearn.datastructures;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

@RunWith( JUnit4.class )
public class BenchmarkRunner {

    private String[] includeClassNames = 
        { //"IndexInsertBenchmark", 
          //"IndexFindBenchmark",
          "SortedArraySetBenchmark"};

    @Test
    public void runBenchmarks() throws RunnerException 
    {
        OptionsBuilder optionsBuilder = new OptionsBuilder();

        for ( String className : includeClassNames )
        {   
            optionsBuilder.include( this.getClass().getPackageName() + "." + className );
        }

        Options options = optionsBuilder
            .forks( 1 )
            .warmupIterations( 5 )
            .measurementIterations( 5 )
            .mode( Mode.AverageTime )
            .timeUnit( TimeUnit.MILLISECONDS )
            .verbosity( VerboseMode.NORMAL )
            .build();

        new Runner( options ).run();
    }
}