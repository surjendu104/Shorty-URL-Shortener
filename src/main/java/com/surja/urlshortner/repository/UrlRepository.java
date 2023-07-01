package com.surja.urlshortner.repository;

import com.surja.urlshortner.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.tags.form.SelectTag;

import java.util.Set;

@Repository
public interface UrlRepository extends MongoRepository<Url, Integer> {
    public Url findByShortUrl(String shortUrl);

}
