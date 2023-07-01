package com.surja.urlshortner.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(value = "url")
@Data
@ToString
@NoArgsConstructor
public class Url {
    @Transient
    public static final String SEQUENCE_NAME = "url_sequence";
    @Id
    private int id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private Set<String> users = new HashSet<>();
}
