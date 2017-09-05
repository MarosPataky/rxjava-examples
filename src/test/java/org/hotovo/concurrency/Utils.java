package org.hotovo.concurrency;

import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;

/**
 * TODO
 */
public class Utils {

    public static AggregatedBookInfo toAggregateBookInfo(Book book, Price price, Rating rating) {
        AggregatedBookInfo aggregatedBookInfo = new AggregatedBookInfo();
        aggregatedBookInfo.setAuthor(book.getAuthor());
        aggregatedBookInfo.setTitle(book.getTitle());
        aggregatedBookInfo.setPrice(price.getPrice());
        aggregatedBookInfo.setDiscount(price.getDiscount());
        aggregatedBookInfo.setRating(rating.getRating());

        return aggregatedBookInfo;
    }
}
