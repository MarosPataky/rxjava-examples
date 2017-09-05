package org.hotovo.service;

import io.reactivex.Observable;
import org.hotovo.model.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 */
public class DescriptionServiceImpl {

    private long delay = 500;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);

    public Observable<Description> getDescriptionForBookObservable(Long bookId) {
        return Observable.just(bookId).map(id -> {
            return getDescriptionForBook(bookId);
        });
    }

    public Description getDescriptionForBook(Long bookId) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        String ratingResourceUrl
                = "http://localhost:3000/descriptions/" + bookId;
        ResponseEntity<Description> response
                = restTemplate.exchange(ratingResourceUrl, HttpMethod.GET, null, Description.class);

        Description description = response.getBody();
        LOGGER.info("Fetched description for book with id {}.", description.getId());
        return description;
    }
}
