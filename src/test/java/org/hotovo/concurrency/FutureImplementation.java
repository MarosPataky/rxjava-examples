package org.hotovo.concurrency;

import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * TODO
 */
public class FutureImplementation {

    private static final ExecutorService pool = Executors.newCachedThreadPool();

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

		return listOfFoundBooks.stream().map(book -> {
			Future<Price> priceFuture = getPriceForBookFuture(book.getId());
			Future<Rating> ratingFuture = getRatingForBookFuture(book.getId());
			return pool.submit(() -> Utils.toAggregateBookInfo(book, priceFuture.get(), ratingFuture.get()));
		})
		.collect(Collectors.toList())
		.stream()
		.map(f -> {
			try {
				return f.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
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
