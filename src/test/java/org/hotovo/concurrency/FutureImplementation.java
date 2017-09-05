package org.hotovo.concurrency;

import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TODO
 */
public class FutureImplementation {

    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    private static final BookServiceImpl bookService = new BookServiceImpl();
    private static final PriceServiceImpl priceService = new PriceServiceImpl();
    private static final RatingServiceImpl ratingService = new RatingServiceImpl();

    public static List<AggregatedBookInfo> getAggregatedBookInfo() {

        Future<List<Book>> books = findBooksFuture();

        List<Book> listOfFoundBooks = null;
        try {
            listOfFoundBooks = books.get(); // blocks here
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<AggregatedBookInfo> aggregatedBookInfoResults = new ArrayList<>();

        for (Book book : listOfFoundBooks) {
            Future<Price> priceFuture = getPriceForBookFuture(book.getId());
            Future<Rating> ratingFuture = getRatingForBookFuture(book.getId());

            try {
                aggregatedBookInfoResults.add(Utils.toAggregateBookInfo(book, priceFuture.get(), ratingFuture.get())); // blocks here
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return aggregatedBookInfoResults;
    }

    private static Future<Rating> getRatingForBookFuture(Long id) {
        return pool.submit(() -> ratingService.getRatingForBook(id));
    }

    private static Future<Price> getPriceForBookFuture(Long id) {
        return pool.submit(() -> priceService.getPriceForBook(id));
    }

    private static Future<List<Book>> findBooksFuture() {
        return pool.submit(bookService::findBooks);
    }
}
