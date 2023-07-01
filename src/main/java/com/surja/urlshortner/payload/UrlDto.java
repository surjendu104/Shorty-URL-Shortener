package com.surja.urlshortner.payload;

import com.surja.urlshortner.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UrlDto {
    private int id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private Set<String> userDtos = new HashSet<>();
}
