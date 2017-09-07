package org.hotovo.concurrency;

import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 */
public class JavaStreamsParallelImplementation {
    private static final BookServiceImpl bookService = new BookServiceImpl();
    private static final PriceServiceImpl priceService = new PriceServiceImpl();
    private static final RatingServiceImpl ratingService = new RatingServiceImpl();

    public static List<AggregatedBookInfo> getAggregatedBookInfo() {

        List<Book> books = bookService.findBooks();

        return books.parallelStream().map(book -> {
            Price price = priceService.getPriceForBook(book.getId());
            Rating rating = ratingService.getRatingForBook(book.getId());
            return Utils.toAggregateBookInfo(book, price, rating);
        }).collect(Collectors.toList());
    }
}
