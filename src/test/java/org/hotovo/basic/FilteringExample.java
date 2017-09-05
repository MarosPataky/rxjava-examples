package org.hotovo.basic;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 */
public class FilteringExample {
    @Test
    public void testFilter() throws Exception {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(value -> value % 2 == 0)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));
    }

    @Test
    public void testDistinct() throws Exception {
        Observable.just(1, 2, 2, 3, 1, 4, 5, 6, 7, 5)
                .distinct()
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));
    }

    @Test
    public void testTake() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(5)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        countDownLatch::countDown);

        countDownLatch.await(10, TimeUnit.SECONDS);

    }

    @Test
    public void testTakeWithTime() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        countDownLatch::countDown);

        countDownLatch.await(10, TimeUnit.SECONDS);
    }

}
