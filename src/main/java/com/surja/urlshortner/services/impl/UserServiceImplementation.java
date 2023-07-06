package com.surja.urlshortner.services.impl;

import com.surja.urlshortner.model.Url;
import com.surja.urlshortner.model.User;
import com.surja.urlshortner.payload.UserDto;
import com.surja.urlshortner.repository.UrlRepository;
import com.surja.urlshortner.repository.UserRepository;
import com.surja.urlshortner.security.JwtHelper;
import com.surja.urlshortner.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.surja.urlshortner.model.User.SEQUENCE_NAME;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UrlServiceImplementation urlServiceImplementation;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Override
    public UserDto getUserDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = helper.getUsernameFromToken(token);
        User user = this.userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found with username : " + username));
        Set<Url> urls = findByUsername(username);
        user.setUrls(urls);
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUserId(this.urlServiceImplementation.getSequenceNumber(SEQUENCE_NAME));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userDto.getRoles().isEmpty())user.setRoles(new ArrayList<String>(){{add("NORMAL");}});

        User user1 = findValueByKeyInDB("email", userDto.getEmail());
        if(user1 != null) {
            throw new RuntimeException("User already exists!!");
        }
        this.userRepository.save(user);
        return this.modelMapper.map(user, UserDto.class);
    }

    private Set<Url> findByUsername(String username) {
        Set<Url> result = new HashSet<>();
        List<Url> allUrls = this.urlRepository.findAll();
        for(Url url : allUrls) {
            if(url.getUsers().contains(username)) {
                result.add(url);
            }
        }
        return result;
    }

    public User findValueByKeyInDB(String key, String value) {
        Query query = new Query(Criteria.where(key).is(value));
        List<User> counter = mongoOperations.find(query, User.class);
        return counter.size()!=0?counter.get(0):null;
    }
}
