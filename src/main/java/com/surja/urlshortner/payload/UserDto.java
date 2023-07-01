package com.surja.urlshortner.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
    private int id;
    private String name;
    private String email;
    private String password;
    private Set<UrlDto> urls = new HashSet<>();
}
