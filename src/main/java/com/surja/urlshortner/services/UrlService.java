package com.surja.urlshortner.services;

import com.surja.urlshortner.exception.ApiErrorException;
import com.surja.urlshortner.payload.UrlDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UrlService {
    UrlDto generateShortUrl(UrlDto urlDto);
    UrlDto getOriginalUrl(String shortUrl);
    UrlDto generateShortUrl(UrlDto urlDto, HttpServletRequest request);
}
