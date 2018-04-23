package com.pany.blog.dtos;

import com.pany.blog.model.Post;
import com.pany.blog.model.Role;

import java.util.List;
import java.util.Set;

public class UserDto {

    public Long id;
    public String login;
    public String email;
    public Set<Role> roles;
    public boolean changePassword;
    public List<Post> posts;

}
