package org.hotovo.basic;

import io.reactivex.Observable;
import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * TODO
 */
public class TransformingExample {
    @Test
    public void testFlatMap() throws Exception {
        create().flatMap(name -> Observable.fromArray(StringUtils.split(name, ", ")))
                .subscribe(System.out::println);
    }

    @Test
    public void testMap() throws Exception {
        createNamesOnly()
                .map(name -> "Character " + name)
                .map(text -> text + " from Star Wars.")
                .subscribe(System.out::println);
    }

    public static Observable<String> create() {
        return Observable.just("Solo, Han", "Skywalker, Anakin", "Skywalker, Luke", "Kenobi, Obi-wan");
    }

    public static Observable<String> createNamesOnly() {
        return Observable.just("Han", "Anakin", "Luke", "Obi-wan");
    }
}
