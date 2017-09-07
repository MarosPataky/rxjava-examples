package org.hotovo.service;

import org.hotovo.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
public class RatingServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);

    public Rating getRatingForBook(Long bookId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        String ratingResourceUrl
                = "http://localhost:3000/ratings/" + bookId;
        ResponseEntity<Rating> response
                = restTemplate.exchange(ratingResourceUrl, HttpMethod.GET, null, Rating.class);

        Rating rating = response.getBody();
        LOGGER.info("Fetched rating for book with id {}.", rating.getId());
        return rating;
    }
}
