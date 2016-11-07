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

import java.util.*;

@State(Scope.Benchmark)
public class LoopPrimitiveBenchmark {

    private final int values[] = new int[1000];

    @Benchmark
    public void testBasicForLoop(Blackhole b) {
        for (int i = 0; i < values.length; ++i) {
            b.consume(values[i]);
        }
    }

    @Benchmark
    public void testForLoopWithCachedSize(Blackhole b) {
        for (int i = 0, n = values.length; i < n; ++i) {
            b.consume(values[i]);
        }
    }

    @Benchmark
    public void testForLoopWithCachedFinalSize(Blackhole b) {
        final int n = values.length;
        for (int i = 0; i < n; ++i) {
            b.consume(values[i]);
        }
    }

    @Benchmark
    public void testForEach(Blackhole b) {
        for (int value : values) {
            b.consume(value);
        }
    }


    @Benchmark
    public void testForEachLambda(Blackhole b) {
        Arrays.stream(values).forEach((value) -> b.consume(value));
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
     * $ java -cp target/benchmarks.jar de.foconis.misc.benchmark.LoopPrimitiveBenchmark
     *
     * First run:
     *
     * Benchmark                                               Mode  Cnt       Score       Error  Units
     * LoopPrimitiveBenchmark.testBasicForLoop                thrpt    5  194563,014 ±  6832,130  ops/s
     * LoopPrimitiveBenchmark.testForEach                     thrpt    5  275228,726 ±  2068,752  ops/s
     * LoopPrimitiveBenchmark.testForEachLambda               thrpt    5  268151,130 ± 10995,585  ops/s
     * LoopPrimitiveBenchmark.testForLoopWithCachedFinalSize  thrpt    5  170532,005 ±  3445,361  ops/s
     * LoopPrimitiveBenchmark.testForLoopWithCachedSize       thrpt    5  167314,721 ± 22710,943  ops/s
     *
     * Second run:
     * Benchmark                                               Mode  Cnt       Score       Error  Units
     * LoopPrimitiveBenchmark.testBasicForLoop                thrpt    5  195431,785 ±  3959,048  ops/s
     * LoopPrimitiveBenchmark.testForEach                     thrpt    5  274157,359 ±  3009,884  ops/s
     * LoopPrimitiveBenchmark.testForEachLambda               thrpt    5  266471,334 ± 14640,400  ops/s
     * LoopPrimitiveBenchmark.testForLoopWithCachedFinalSize  thrpt    5  169817,617 ±  7567,815  ops/s
     * LoopPrimitiveBenchmark.testForLoopWithCachedSize       thrpt    5  171693,711 ±  1764,275  ops/s
     * </pre>
     * Surprise: forEach seems to be the fastest.
     */
    public static void main(String... args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(LoopPrimitiveBenchmark.class.getSimpleName())
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