package org.hotovo.concurrency;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;

import java.util.List;

/**
 * TODO
 */
public class ObservablesImplementation {

    private static final BookServiceImpl bookService = new BookServiceImpl();
    private static final PriceServiceImpl priceService = new PriceServiceImpl();
    private static final RatingServiceImpl ratingService = new RatingServiceImpl();

    public static List<AggregatedBookInfo> getAggregatedBookInfo() {

        Observable<Book> booksObservable = findBooksObservable();

        Observable<AggregatedBookInfo> aggegatedInfo = booksObservable.flatMap(book -> {
            Observable<Price> priceObservable = getPriceForBookObservable(book.getId()); //.subscribeOn(Schedulers.io());
            Observable<Rating> ratingObservable = getRatingForBookOservable(book.getId());//.subscribeOn(Schedulers.io());
            return Observable.zip(priceObservable, ratingObservable,
                    (p, r) -> Utils.toAggregateBookInfo(book, p, r));
        });

        return aggegatedInfo.toList().blockingGet();
    }

    public static Observable<Book> findBooksObservable() {
        return Observable.fromCallable(bookService::findBooks)
                .flatMapIterable(x -> x);
    }

    public static Observable<Price> getPriceForBookObservable(Long bookId) {
        return Observable.just(bookId).map(id -> priceService.getPriceForBook(bookId)).subscribeOn(Schedulers.io());
    }

    public static Observable<Rating> getRatingForBookOservable(Long bookId) {
        return Observable.just(bookId).map(id -> ratingService.getRatingForBook(bookId)).subscribeOn(Schedulers.io());
    }
}
