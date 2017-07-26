package org.hotovo.service;

import io.reactivex.Observable;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *
 */
public class PriceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksService.class);

    public Observable<Price> getPriceForBookObservable(Long bookId) {
        return Observable.just(bookId).map(id -> {
            return getPriceForBook(bookId);
        });
    }

    public Price getPriceForBook(Long bookId) {
        RestTemplate restTemplate = new RestTemplate();
        String booksResourceUrl
                = "http://localhost:3000/books/" + bookId;
        ResponseEntity<Price> response
                = restTemplate.exchange(booksResourceUrl, HttpMethod.GET, null, Price.class);

        Price price = response.getBody();
        LOGGER.info("Fetched price for book with id %d.", price.getId());
        return price;
    }
}
