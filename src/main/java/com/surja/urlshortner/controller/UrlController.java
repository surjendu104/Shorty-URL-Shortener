package com.surja.urlshortner.controller;

import com.surja.urlshortner.payload.ApiResponse;
import com.surja.urlshortner.payload.UrlDto;
import com.surja.urlshortner.services.impl.UrlServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/home")
public class UrlController {

    @Autowired
    UrlServiceImplementation urlServiceImplementation;
    @PostMapping("/saveurl")
    public ResponseEntity<?> saveUrl(@RequestBody UrlDto urlDto, HttpServletRequest request){
        if(urlDto.getOriginalUrl() == null) {
            return ResponseEntity.ok(new ApiResponse("URL Can not be NULL",true,400));
        }
        UrlDto responseUrl = this.urlServiceImplementation.generateShortUrl(urlDto, request);
        return ResponseEntity.ok(responseUrl);
    }
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        UrlDto url = this.urlServiceImplementation.getOriginalUrl(shortUrl);
        if(!this.urlServiceImplementation.checkExpirationOfUrl(url)) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("URL is Expired", false, 400), HttpStatus.NOT_FOUND);

        }
        else {
            response.sendRedirect(url.getOriginalUrl());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

    }

}

