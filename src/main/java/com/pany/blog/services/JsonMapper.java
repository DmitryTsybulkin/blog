package com.pany.blog.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pany.blog.dtos.RoleDto;
import com.pany.blog.dtos.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class JsonMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String toJson(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("ERROR SUBMITTING AN ENTITY TO A JSON");
        }
        return null;
    }

    public List<RoleDto> toRole(final InputStream json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<RoleDto>>(){});
        } catch (IOException e) {
            logger.error("ERROR SUBMITTING AN ROLE-JSON TO A DB");
        }
        return null;
    }

    public List<UserDto> toUser(final InputStream json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<UserDto>>(){});
        } catch (IOException e) {
            logger.error("ERROR SUBMITTING AN USER-JSON TO A DB");
            e.printStackTrace();
        }
        return null;
    }

}
