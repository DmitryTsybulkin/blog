package com.pany.blog.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pany.blog.model.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class JsonMapper {

    public static String toJson(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BlogAttrs> toBlogAttrs(final File json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<BlogAttrs>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Comment> toComment(final File json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<Comment>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Post> toPost(final File json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<Post>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Role> toRole(final File json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<Role>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> toUser(final File json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<User>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
