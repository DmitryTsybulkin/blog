package com.pany.blog.services;

import com.pany.blog.dtos.RoleDto;
import com.pany.blog.dtos.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonMapperTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void toJson() throws Exception {
        UserDto dto = new UserDto();
        dto.id = 1L;
        dto.login = "login";
        dto.email = "email";
        dto.roles = null;
        dto.password = "password";
        String targetJson = "{\"id\":1," +
                "\"login\":\"login\"," +
                "\"email\":\"email\"," +
                "\"password\":\"password\"," +
                "\"roles\":null}";
        String testJson = jsonMapper.toJson(dto);
        assertEquals(targetJson, testJson);
    }

    @Test
    public void toRole() throws Exception {
        String role = "[{\"name\":\"ADMIN_ROLE\"}]";
        InputStream in = new ByteArrayInputStream(role.getBytes());
        List<RoleDto> roles = jsonMapper.toRole(in);
        assertEquals(roles.get(0).name, "ADMIN_ROLE");
    }

    @Test
    public void toUser() throws Exception {
        String user = "[{\"id\":5," +
                "\"login\":\"admin\"," +
                "\"password\":\"password\"," +
                "\"email\":\"mail\"," +
                "\"roles\":[\"ADMIN_ROLE\"]}]";
        InputStream in = new ByteArrayInputStream(user.getBytes());
        List<UserDto> userDtos = jsonMapper.toUser(in);
        assertEquals(userDtos.get(0).login, "admin");
        assertEquals(userDtos.get(0).email, "mail");
        assertEquals(userDtos.get(0).password, "password");
        assertEquals(userDtos.get(0).roles.get(0), "ADMIN_ROLE");
    }

}