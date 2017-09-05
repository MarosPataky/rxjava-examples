package org.hotovo.basic;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 */
public class CreatingExample {

    @Test
    public void createOperation() {
        Observable observable = Observable.create(subscriber -> {
            try {
                System.out.println("Running");
                subscriber.onNext("first item");
                System.out.println("Running 2");
                subscriber.onNext("second item");
                subscriber.onNext("3rd item");
                subscriber.onNext("second item");
                subscriber.onNext("...");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).distinct();

        observable.subscribeOn(Schedulers.newThread()).subscribe(next -> System.out.println("first next: " + next),
                (a) -> {
                    System.out.println("aaa");
                },
                () -> System.out.println("first complete!"));

        observable.subscribeOn(Schedulers.newThread()).subscribe(next -> System.out.println("next: " + next),
                (a) -> {
                    System.out.println("aaa");
                },
                () -> System.out.println("complete!"));
    }

    @Test
    public void testJust() throws Exception {
        Observable.just("first")
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));

        Observable.just("first", "second", "3rd", "...")
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));
    }

    @Test
    public void testFromArray() throws Exception {
        String[] array = {"1", "2"};
        Observable.fromArray(array)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));
    }

    @Test
    public void testFromIterable() throws Exception {
        Iterable<String> iterable = Arrays.asList("1", "2");
        Observable.fromIterable(iterable)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));
    }

    @Test
    public void testInterval() throws InterruptedException {
        Observable.<String>interval(200, TimeUnit.MILLISECONDS)
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));

        Thread.sleep(1000);
    }


}
