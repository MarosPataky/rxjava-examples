package org.hotovo;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.hotovo.model.AggregatedBookInfo;
import org.hotovo.model.Book;
import org.hotovo.model.Price;
import org.hotovo.model.Rating;
import org.hotovo.service.BookServiceImpl;
import org.hotovo.service.PriceServiceImpl;
import org.hotovo.service.RatingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RxjavaExamplesApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RxjavaExamplesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RxjavaExamplesApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        BookServiceImpl booksService = new BookServiceImpl();
        PriceServiceImpl priceService = new PriceServiceImpl();
        RatingServiceImpl ratingService = new RatingServiceImpl();

        LOGGER.info("GetBooks starting at " + System.currentTimeMillis());

        Observable<Book> booksObservable = booksService.findBooksObservable();

        LOGGER.info("BookObservable created, but not subscribed.");

        booksObservable.flatMap(book -> {
            LOGGER.info("Processing book with id = {}", book.getId());
            return Observable.just(book)
//                    .subscribeOn(Schedulers.io())
                    .map(book1 -> {
                        Observable<Price> price = priceService.getPriceForBookObservable(book.getId()); //.subscribeOn(Schedulers.io());
                        Observable<Rating> rating = ratingService.getRatingForBookOservable(book.getId());//.subscribeOn(Schedulers.io());
                        return Observable.zip(price, rating, (p, r) -> {
                            AggregatedBookInfo aggregatedBookInfo = new AggregatedBookInfo();
                            aggregatedBookInfo.setAuthor(book.getAuthor());
                            aggregatedBookInfo.setTitle(book.getTitle());
                            aggregatedBookInfo.setPrice(p.getPrice());
                            aggregatedBookInfo.setDiscount(p.getDiscount());
                            aggregatedBookInfo.setRating(r.getRating());
                            return aggregatedBookInfo;
                        });
                    });
        }).subscribe(aggregatedBookInfo -> {
            aggregatedBookInfo/*.subscribeOn(Schedulers.io())*/.subscribe(aggregatedBookInfo1 -> {
                LOGGER.info("Aggregated book info: {}", aggregatedBookInfo1.toString());
                LOGGER.info("BookInfo received at: {}", System.currentTimeMillis());
                countDownLatch.countDown();
            });
        });

        countDownLatch.await(10, TimeUnit.SECONDS);
        stopWatch.stop();
        System.out.println("Time to aggregate book info " + stopWatch.getTotalTimeMillis());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}


//package org.hotovo;
//
//import org.hotovo.model.Book;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//public class RxjavaExamplesApplication {
//
//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        String booksResourceUrl
//                = "http://localhost:3000/books";
//        ResponseEntity<List<Book>> response
//                = restTemplate.exchange(booksResourceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
//        });
//    }
//}
