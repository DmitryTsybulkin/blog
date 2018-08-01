package com.pany.blog.dtos;

public class BlogAttrsDto {

    public Long key;
    public String value;
    public String description;

    public BlogAttrsDto(Long key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public BlogAttrsDto() {}
}
