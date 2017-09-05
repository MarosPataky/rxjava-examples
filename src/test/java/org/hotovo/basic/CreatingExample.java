package org.hotovo.basic;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 */
public class CreatingExample {

    @Test
    public void createOperation() {
        Observable.create(subscriber -> {
            try {
                subscriber.onNext("first item");
                subscriber.onNext("second item");
                subscriber.onNext("3rd item");
                subscriber.onNext("...");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })
                .subscribe(next -> System.out.println("next: " + next),
                        Throwable::printStackTrace,
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
                .subscribe(next -> System.out.println("next: " +  next),
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
                .subscribe(next -> System.out.println("next: " +  next),
                        Throwable::printStackTrace,
                        () -> System.out.println("complete!"));

        Thread.sleep(1000);
    }


}
