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

import java.util.List;
import java.util.stream.Collectors;

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
        logger.info("Created new blog attributes entry.");
    }

    @Transactional(readOnly = true)
    public BlogAttrsDto getBlogAttrsById(final Long key) {
        return toDto(blogAttrsRep.findById(key).orElseThrow(ResourceNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public BlogAttrsDto getBlogAttrsByValue(final String value) {
        return toDto(blogAttrsRep.getBlogAttrsByValue(value).orElseThrow(ResourceNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public List<BlogAttrsDto> getBlogAttrsList() {
        return blogAttrsRep.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateBlogAttrs(final BlogAttrsDto dto) {
        BlogAttrs blogAttrs = blogAttrsRep.findById(dto.key).orElseThrow(ResourceNotFoundException::new);
        blogAttrs.setValue(dto.value);
        blogAttrs.setDescription(dto.description);
        logger.info("Blog attributes: " + blogAttrs.getKey() + " updated successfully.");
    }

    @Transactional
    public void deleteBlogAttrs(final Long key) {
        blogAttrsRep.delete(blogAttrsRep.findById(key).orElseThrow(ResourceNotFoundException::new));
        logger.info("Blog attributes: " + key + " deleted successfully.");
    }

    public BlogAttrsDto toDto(final BlogAttrs blogAttrs) {
        return new BlogAttrsDto(blogAttrs.getKey(), blogAttrs.getValue(), blogAttrs.getDescription());
    }

    public BlogAttrs fromDto(final BlogAttrsDto dto) {
        return new BlogAttrs(dto.value, dto.description);
    }

}
