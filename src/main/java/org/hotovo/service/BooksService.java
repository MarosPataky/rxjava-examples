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
public class BooksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksService.class);

    public Observable<Book> findBooksObservable() {
        return Observable.fromCallable(() -> {
            return findBooks();
        }).flatMapIterable(x -> x);
    }

    public List<Book> findBooks() {
        RestTemplate restTemplate = new RestTemplate();
        String booksResourceUrl
                = "http://localhost:3000/books";
        ResponseEntity<List<Book>> response
                = restTemplate.exchange(booksResourceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
        });

        List<Book> books = response.getBody();
        LOGGER.info("Fetched %d books.", books.size());
        return books;
    }
}
