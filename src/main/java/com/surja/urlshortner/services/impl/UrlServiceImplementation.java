package com.surja.urlshortner.services.impl;

import com.google.common.hash.Hashing;
import com.surja.urlshortner.model.DbSequence;
import com.surja.urlshortner.model.Url;
import com.surja.urlshortner.payload.UrlDto;
import com.surja.urlshortner.repository.UrlRepository;
import com.surja.urlshortner.repository.UserRepository;
import com.surja.urlshortner.security.JwtHelper;
import com.surja.urlshortner.services.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static com.surja.urlshortner.model.Url.SEQUENCE_NAME;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class UrlServiceImplementation implements UrlService {
    private char[] myChars;
    @Value("${SHORT_URL_KEY_LENGTH}")
    private int keyLength;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper helper;

    @Override
    public UrlDto generateShortUrl(UrlDto urlDto){
        if(!Objects.equals(urlDto.getOriginalUrl(), "")) {
            LocalDateTime time = LocalDateTime.now();
            String shortUrl = encodeIt(urlDto.getOriginalUrl().concat(time.toString()));

            Url newUrl = new Url();

            String sanitizedUrl = sanitizeUrl(urlDto.getOriginalUrl());
            if(findValueByKeyInDB("originalUrl",sanitizedUrl)!=null) {
                newUrl = findValueByKeyInDB("originalUrl",sanitizedUrl);
            }else {
                newUrl.setOriginalUrl(sanitizeUrl(urlDto.getOriginalUrl()));
                newUrl.setId(getSequenceNumber(SEQUENCE_NAME));
                newUrl.setShortUrl(shortUrl);
                newUrl.setCreationDate(time);
                newUrl.setExpirationDate(time.plusDays(120));
                this.urlRepository.save(newUrl);
            }

            return this.modelMapper.map(newUrl, UrlDto.class);
        }
        return new UrlDto();
    }

    @Override
    public UrlDto getOriginalUrl(String shortUrl) throws IllegalArgumentException {
        Url url = this.urlRepository.findByShortUrl(shortUrl);
        return this.modelMapper.map(url, UrlDto.class);
    }

    @Override
    public UrlDto generateShortUrl(UrlDto urlDto, HttpServletRequest request) {
        if(!Objects.equals(urlDto.getOriginalUrl(), "")) {
            LocalDateTime time = LocalDateTime.now();
            String shortUrl = encodeIt(urlDto.getOriginalUrl().concat(time.toString()));

            Url newUrl = new Url();

            String token = request.getHeader("Authorization").substring(7);
            String username = helper.getUsernameFromToken(token);

            String sanitizedUrl = sanitizeUrl(urlDto.getOriginalUrl());
            if(findValueByKeyInDB("originalUrl",sanitizedUrl)!=null) {
                newUrl = findValueByKeyInDB("originalUrl",sanitizedUrl);
            }else {
                newUrl.setOriginalUrl(sanitizeUrl(urlDto.getOriginalUrl()));
                newUrl.setId(getSequenceNumber(SEQUENCE_NAME));
                newUrl.setShortUrl(shortUrl);
                newUrl.setCreationDate(time);
                newUrl.setExpirationDate(time.plusDays(120));
            }
            if(username != null) {
                Set<String> users = newUrl.getUsers();
                users.add(username);
                newUrl.setUsers(users);
            }
            this.urlRepository.save(newUrl);
            return this.modelMapper.map(newUrl, UrlDto.class);
        }
        return new UrlDto();
    }

    private String encodeUrl(String originalUrl, LocalDateTime time) {
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32().hashString(originalUrl.concat(time.toString()), StandardCharsets.UTF_8).toString();
        return encodedUrl;
    }

    private String encodeIt(String originalUrl) {
        String shortUrl = "";
        createIndexingTable();
        shortUrl = generateKey();
        return shortUrl;
    }

    private void createIndexingTable() {
        myChars = new char[62];
        for (int i = 0; i < 62; i++) {
            int j = 0;
            if (i < 10) {
                j = i + 48;
            } else if (i > 9 && i <= 35) {
                j = i + 55;
            } else {
                j = i + 61;
            }
            myChars[i] = (char) j;
        }
    }
    private String generateKey() {
        String key = "";
        boolean flag = true;
        while(flag) {
            key = "";
            for(int i = 0; i < keyLength; ++i) {
                key+=myChars[new Random().nextInt(62)];
            }
            if(!findKeyInDb(key)) {
                flag = false;
            }
        }
        return key;
    }
    private boolean findKeyInDb(String key) {
        Query query = new Query(Criteria.where("shortUrl").is(key));
        List<Url> counter = mongoOperations.find(query, Url.class);
        return counter.size()!=0;
    }
    public String sanitizeUrl(String url) {
        if(url.length()<8) {
            return "https://"+url;
        }
        String httpString = url.substring(0,8);
        if(!httpString.equals("https://")) {
            return "https://"+url;
        }
        return url;
    }
    public boolean checkExpirationOfUrl(UrlDto urlDto) {
        if(urlDto.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
    public int getSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);

        DbSequence counter = mongoOperations
                .findAndModify(query, update,
                        options().returnNew(true).
                                upsert(true), DbSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
    public Url findValueByKeyInDB(String key, String value) {
        Query query = new Query(Criteria.where(key).is(value));
        List<Url> counter = mongoOperations.find(query, Url.class);
        return counter.size()!=0?counter.get(0):null;
    }
}
