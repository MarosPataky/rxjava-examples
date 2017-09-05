package org.hotovo.basic;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 */
public class CombiningExample {

    @Test
    public void testMergeWith() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        stream1.mergeWith(stream2)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        countDownLatch::countDown);

        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testZip() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Observable.zip(stream1, stream2, (s1, s2) -> '{' + s1 + " + " + s2 + '}')
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        countDownLatch::countDown);

        countDownLatch.await(10, TimeUnit.SECONDS);
    }
}
