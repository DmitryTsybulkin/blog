package com.pany.blog.services;

import com.pany.blog.dtos.BlogAttrsDto;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.BlogAttrs;
import com.pany.blog.repositories.BlogAttrsRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogAttrsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BlogAttrsRep blogAttrsRep;

    @Autowired
    public BlogAttrsService(final BlogAttrsRep blogAttrsRep) {
        this.blogAttrsRep = blogAttrsRep;
    }

    @Transactional
    public void createBlogAttrs(final BlogAttrsDto dto) {
        blogAttrsRep.save(new BlogAttrs(dto.value, dto.description));
        logger.info("CREATED NEW BLOG ATTRS ENTRY");
    }

    @Transactional(readOnly = true)
    public BlogAttrsDto getBlogAttrsById(final Long key) {
        BlogAttrs blogAttrs = blogAttrsRep.findById(key).orElseThrow(ResourceNotFoundException::new);
        return toDto(blogAttrs);
    }

    @Transactional(readOnly = true)
    public BlogAttrsDto getBlogAttrsByValue(final String value) {
        return toDto(blogAttrsRep.getBlogAttrsByValue(value));
    }

    @Transactional(readOnly = true)
    public List<BlogAttrsDto> getBlogAttrsList() {
        List<BlogAttrs> blogAttrs = blogAttrsRep.findAll();
        List<BlogAttrsDto> blogAttrsDtos = new ArrayList<>();
        blogAttrs.forEach(blogAttrsElement -> blogAttrsDtos.add(toDto(blogAttrsElement)));
        return blogAttrsDtos;
    }

    @Transactional
    public void updateBlogAttrs(final BlogAttrsDto dto) {
        BlogAttrs blogAttrs = blogAttrsRep.findById(dto.key).orElseThrow(ResourceNotFoundException::new);
        blogAttrs.setValue(dto.value);
        blogAttrs.setDescription(dto.description);
    }

    @Transactional
    public void deleteBlogAttrs(final Long key) {
        blogAttrsRep.delete(blogAttrsRep.findById(key).orElseThrow(ResourceNotFoundException::new));
    }

    public BlogAttrsDto toDto(final BlogAttrs blogAttrs) {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.key = blogAttrs.getKey();
        dto.value = blogAttrs.getValue();
        dto.description = blogAttrs.getDescription();
        return dto;
    }

    public BlogAttrs fromDto(final BlogAttrsDto dto) {
        return new BlogAttrs(dto.value, dto.description);
    }

}
