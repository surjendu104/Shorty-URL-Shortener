package com.surja.urlshortner.repository;

import com.surja.urlshortner.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url, Integer> {
    Url findByShortUrl(String shortUrl);

}
