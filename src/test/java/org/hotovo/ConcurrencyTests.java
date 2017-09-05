package org.hotovo;

import org.hotovo.concurrency.CompletableFutureImplementation;
import org.hotovo.concurrency.FutureImplementation;
import org.hotovo.concurrency.JavaStreamsParallelImplementation;
import org.hotovo.concurrency.ObservablesImplementation;
import org.hotovo.concurrency.SynchronousImplementation;
import org.hotovo.model.AggregatedBookInfo;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * TODO
 */
public class ConcurrencyTests {

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void testSynchronousImplementation() {
        List<AggregatedBookInfo> listOfAggregatedBookInfo = SynchronousImplementation.getAggregatedBookInfo();
        System.out.println("Fetched all " + listOfAggregatedBookInfo.size()  + " books");
    }

    @Test
    public void testFutureImplementation() {
        List<AggregatedBookInfo> listOfAggregatedBookInfo = FutureImplementation.getAggregatedBookInfo();
        System.out.println("Fetched all " + listOfAggregatedBookInfo.size()  + " books");
    }

    @Test
    public void testJavaStreamsParallelImplementation() {
        List<AggregatedBookInfo> listOfAggregatedBookInfo = JavaStreamsParallelImplementation.getAggregatedBookInfo();
        System.out.println("Fetched all " + listOfAggregatedBookInfo.size()  + " books");
    }

    @Test
    public void testCompletableFutureImplementation() throws ExecutionException, InterruptedException {
        List<AggregatedBookInfo> listOfAggregatedBookInfo = CompletableFutureImplementation.getAggregatedBookInfo();
        System.out.println("Fetched all " + listOfAggregatedBookInfo.size()  + " books");
    }

    @Test
    public void testObservablesImplementation() {
        List<AggregatedBookInfo> listOfAggregatedBookInfo = ObservablesImplementation.getAggregatedBookInfo();
        System.out.println("Fetched all " + listOfAggregatedBookInfo.size()  + " books");
    }
}
