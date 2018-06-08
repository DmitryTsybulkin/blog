package com.pany.blog.dtos;

import java.util.List;

public class UserDto {

    public Long id;
    public String login;
    public String email;
    public String password;
    public List<String> roles;

    public UserDto(Long id, String login, String email, List<String> roles) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.roles = roles;
    }

    public UserDto() {}
}
