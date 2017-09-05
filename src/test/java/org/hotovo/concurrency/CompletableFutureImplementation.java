package org.hotovo.concurrency;

import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 */
public class CompletableFutureImplementation {

    private static final ExecutorService pool = Executors.newFixedThreadPool(10);
    private static final ExecutorService pool2 = Executors.newFixedThreadPool(10);

    private static final BookServiceImpl bookService = new BookServiceImpl();
    private static final PriceServiceImpl priceService = new PriceServiceImpl();
    private static final RatingServiceImpl ratingService = new RatingServiceImpl();

    public static List<AggregatedBookInfo> getAggregatedBookInfo() throws ExecutionException, InterruptedException {

        return findBooksCompletableFuture()
                .thenApply(listOfBooks -> listOfBooks
                        .parallelStream()
                        .map(book -> {
                            CompletableFuture<Price> getPriceCompletableFuture = getPriceForBookCompletableFuture(book.getId());
                            CompletableFuture<Rating> getRatingCompletableFuture = getRatingForBookCompletableFuture(book.getId());

                            CompletableFuture.allOf(getPriceCompletableFuture, getRatingCompletableFuture);

                            try {
                                return Utils.toAggregateBookInfo(book, getPriceCompletableFuture.get(), getRatingCompletableFuture.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collect(Collectors.toList())).get();
    }

    private static CompletableFuture<Rating> getRatingForBookCompletableFuture(Long id) {
        return CompletableFuture.supplyAsync(() -> ratingService.getRatingForBook(id), pool2);
    }

    private static CompletableFuture<Price> getPriceForBookCompletableFuture(Long id) {
        return CompletableFuture.supplyAsync(() -> priceService.getPriceForBook(id), pool);
    }

    private static CompletableFuture<List<Book>> findBooksCompletableFuture() {
        return CompletableFuture.supplyAsync(() -> bookService.findBooks(), pool);
    }
}
