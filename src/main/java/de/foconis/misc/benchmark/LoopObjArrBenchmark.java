package de.foconis.misc.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@State(Scope.Benchmark)
public class LoopObjArrBenchmark {

    private final Integer[] values = new ArrayList<>(Collections.nCopies(1_000, 42)).toArray(new Integer[1_000]);

    @Benchmark
    public void testBasicForLoop(Blackhole b) {
        for (int i = 0; i < values.length; ++i) {
            b.consume(values[i]);
        }
    }

    @Benchmark
    public void testForEach(Blackhole b) {
        for (Integer value : values) {
            b.consume(value);
        }
    }

    /**
     * <pre>
     * # HOST: Windows 10 - 64bit. Core i5 M520 2.40 GHz. 4 GB RAM
     * $ java -version
     * java version "1.8.0_101"
     * Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
     * Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
     *
     * $ mvn clean install
     * $ java -cp target/benchmarks.jar de.foconis.misc.benchmark.LoopObjArrBenchmark
     *
     * </pre>
     *
     * @param args
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(LoopObjArrBenchmark.class.getSimpleName())
                .mode(Mode.Throughput)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(5))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(5))
                .jvmArgs("-server")
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
