package com.pany.blog.services;

import com.pany.blog.dtos.BlogAttrsDto;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.BlogAttrs;
import com.pany.blog.repositories.BlogAttrsRep;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogAttrsServiceTest {

    @Autowired
    private BlogAttrsService blogAttrsService;

    @Autowired
    private BlogAttrsRep blogAttrsRep;

    @Test
    public void createBlogAttrs() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.value = "blogName";
        dto.description = "blogDescription";
        blogAttrsService.createBlogAttrs(dto);
        BlogAttrsDto targetDto = blogAttrsService.getBlogAttrsByValue(dto.value);
        assertEquals(targetDto.description, dto.description);
        assertNotNull(targetDto.key);
    }

    @Test
    public void getBlogAttrsById() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.value = "blogName";
        dto.description = "blogDescription";
        blogAttrsService.createBlogAttrs(dto);
        BlogAttrsDto pointDto = blogAttrsService.getBlogAttrsByValue(dto.value);

        BlogAttrsDto targetDto = blogAttrsService.getBlogAttrsById(pointDto.key);
        assertEquals(targetDto.description, dto.description);
        assertEquals(targetDto.value, dto.value);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getBlogAttrsByIdFailedEntryDoesntExists() throws Exception {
        blogAttrsService.getBlogAttrsById(5L);
    }

    @Test
    public void getBlogAttrsList() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.value = "blogName";
        dto.description = "blogDescription";
        blogAttrsService.createBlogAttrs(dto);

        BlogAttrsDto anotherDto = new BlogAttrsDto();
        dto.value = "anotherBlogName";
        dto.description = "anotherBlogDescription";
        blogAttrsService.createBlogAttrs(dto);

        List<BlogAttrsDto> dtoList = blogAttrsService.getBlogAttrsList();
        for (BlogAttrsDto blogAttrsDto : dtoList) {
            assertNotNull(blogAttrsDto.key);
        }
    }

    @Test
    public void updateBlogAttrs() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.value = "blogName";
        dto.description = "blogDescription";
        blogAttrsService.createBlogAttrs(dto);
        BlogAttrsDto pointDto = blogAttrsService.getBlogAttrsByValue(dto.value);
        assertEquals(pointDto.description, dto.description);

        BlogAttrsDto targetDto = new BlogAttrsDto();
        targetDto.key = pointDto.key;
        targetDto.value = "newValue";
        targetDto.description = "newDescription";

        blogAttrsService.updateBlogAttrs(targetDto);

        BlogAttrsDto resultDto = blogAttrsService.getBlogAttrsById(pointDto.key);
        assertEquals(resultDto.value, targetDto.value);
        assertEquals(resultDto.description, targetDto.description);
    }

    @Test
    public void deleteBlogAttrs() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.value = "blogName";
        dto.description = "blogDescription";
        blogAttrsService.createBlogAttrs(dto);
        BlogAttrsDto pointDto = blogAttrsService.getBlogAttrsByValue(dto.value);
        blogAttrsService.deleteBlogAttrs(pointDto.key);
        assertTrue(blogAttrsService.getBlogAttrsList().isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteBlogAttrsFailedEntryDoesntExists() throws Exception {
        blogAttrsService.deleteBlogAttrs(5000L);
    }

    @Test
    public void toDto() throws Exception {
        BlogAttrs blogAttrs = new BlogAttrs("value", "description");
        BlogAttrsDto dto = blogAttrsService.toDto(blogAttrs);
        assertEquals(blogAttrs.getValue(), dto.value);
        assertEquals(blogAttrs.getDescription(), dto.description);
    }

    @Test
    public void fromDto() throws Exception {
        BlogAttrsDto dto = new BlogAttrsDto();
        dto.description = "description";
        dto.value = "value";
        BlogAttrs blogAttrs = blogAttrsService.fromDto(dto);
        assertEquals(blogAttrs.getDescription(), dto.description);
        assertEquals(blogAttrs.getValue(), dto.value);
    }

    @After
    public void dropTable() throws Exception {
        blogAttrsRep.deleteAllInBatch();
    }

}