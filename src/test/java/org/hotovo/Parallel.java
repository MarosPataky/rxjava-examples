package org.hotovo;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 */
public class Parallel {
    /**
     * In this example the the three observables will be emitted sequentially and the three items will be passed to the pipeline
     */
    @Test
    public void testZip() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        long start = System.currentTimeMillis();
        Observable.zip(obString(), obString1(), obString2(), (s, s2, s3) -> s.concat(s2)
                .concat(s3))
                .subscribe(result -> {
                    showResult("Sync in:", start, result);
                    latch.countDown();
                });

        latch.await(3000, TimeUnit.MILLISECONDS);
    }


    public void showResult(String transactionType, long start, String result) {
        System.out.println(result + " " +
                transactionType + String.valueOf(System.currentTimeMillis() - start));
    }

    public Observable<String> obString() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                })
                .map(val -> "Hello");
    }

    public Observable<String> obString1() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .map(val -> " World");
    }

    public Observable<String> obString2() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .map(val -> "!");
    }


    private Scheduler scheduler;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    /**
     * Since every observable into the zip is created to subscribeOn a different thread, it´s means all of them will run in parallel.
     * By default Rx is not async, only if you explicitly use subscribeOn.
     */
    @Test
    public void testAsyncZip() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        scheduler = Schedulers.newThread();
        scheduler1 = Schedulers.newThread();
        scheduler2 = Schedulers.newThread();
        long start = System.currentTimeMillis();
        Observable.zip(obAsyncString(), obAsyncString1(), obAsyncString2(), (s, s2, s3) -> s.concat(s2)
                .concat(s3))
                .subscribe(result -> {
                    showResult("Async in:", start, result);
                    latch.countDown();
                });

        latch.await(3000, TimeUnit.MILLISECONDS);
    }

    private Observable<String> obAsyncString() {
        return Observable.just("")
                .observeOn(scheduler)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " going to sleep!");
                })
                .doOnNext(val -> {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " returned from sleep!");
                })
                .map(val -> "Hello");
    }

    private Observable<String> obAsyncString1() {
        return Observable.just("")
                .observeOn(scheduler1)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " going to sleep!");
                })
                .doOnNext(val -> {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " returned from sleep!");
                })
                .map(val -> " World");
    }

    private Observable<String> obAsyncString2() {
        return Observable.just("")
                .observeOn(scheduler2)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " going to sleep!");
                })
                .doOnNext(val -> {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName() + " returned from sleep!");
                })
                .map(val -> "!");
    }
}
