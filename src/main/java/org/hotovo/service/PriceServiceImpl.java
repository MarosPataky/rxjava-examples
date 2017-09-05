package org.hotovo.service;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.hotovo.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
public class PriceServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceServiceImpl.class);

    public Price getPriceForBook(Long bookId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        String priceResourceUrl
                = "http://localhost:3000/prices/" + bookId;
        ResponseEntity<Price> response
                = restTemplate.exchange(priceResourceUrl, HttpMethod.GET, null, Price.class);

        Price price = response.getBody();
        LOGGER.info("Fetched price for book with id {}.", price.getId());
        return price;
    }
}
