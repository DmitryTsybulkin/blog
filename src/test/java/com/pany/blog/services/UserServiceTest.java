package com.pany.blog.services;

import com.pany.blog.dtos.UserDto;
import com.pany.blog.exceptions.EntryDuplicateException;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Post;
import com.pany.blog.model.PostBuilder;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.PostRep;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRep userRep;

    @Test
    public void createUser() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);
        UserDto targetUser = userService.getUserByLogin(login);
        assertNotNull(targetUser);
        assertNotNull(targetUser.id);
        assertFalse(targetUser.roles.isEmpty());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createUserFailedEntryDuplicate() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);

        UserDto anotherDto = new UserDto();
        anotherDto.email = "anotherEmail";
        anotherDto.login = login;
        userService.createUser(dto);
    }

    @Test
    public void getUserById() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);

        UserDto pointDto = userService.getUserByLogin(login);
        UserDto targetDto = userService.getUserById(pointDto.id);
        assertNotNull(targetDto);
        assertNotNull(targetDto.login);
        assertEquals(targetDto.login, login);
        assertFalse(targetDto.roles.isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByIdFailedUserDoesNotExists() {
        Long id = 5000L;
        userService.getUserById(id);
    }

    @Test
    public void getUsersList() {
        UserDto dto = new UserDto();
        dto.email = "email";
        dto.login = "login";
        userService.createUser(dto);

        UserDto anotherDto = new UserDto();
        anotherDto.email = "anotherEmail";
        anotherDto.login = "anotherLogin";
        userService.createUser(anotherDto);

        List<UserDto> dtos = userService.getUsersList();
        for (UserDto userDto : dtos) {
            assertNotNull(userDto.id);
            assertFalse(userDto.roles.isEmpty());
        }
    }

    @Test
    public void updateUser() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);

        Long id = userService.getUserByLogin(login).id;
        UserDto anotherDto = new UserDto();
        anotherDto.login = "topLogin";
        anotherDto.changePassword = true;
        anotherDto.id = id;
        anotherDto.roles = new HashSet<>();
        userService.updateUser(anotherDto);

        UserDto targetDto = userService.getUserById(id);
        assertEquals(targetDto.login, "topLogin");
        assertFalse(targetDto.roles.isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUser() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);

        Long id = userService.getUserByLogin(login).id;
        userService.deleteUser(id);

        assertNull(userService.getUserById(id));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUserFailedUserDoesNotExists() {
        userService.deleteUser(5000L);
    }

    @Test
    public void toDto() {
        User user = new User("login", "password", new HashSet<>(), new ArrayList<>());
        UserDto dto = userService.toDto(user);
        assertEquals(dto.login, user.getLogin());
        assertNotNull(dto.posts);
        assertNotNull(dto.roles);
    }

    @Test
    public void fromDto() {
        UserDto dto = new UserDto();
        dto.login = "login";
        dto.roles = new HashSet<>();
        dto.posts = new ArrayList<>();
        User user = userService.fromDto(dto, "password");
        assertEquals(user.getLogin(), dto.login);
        assertNotNull(user.getRoles());
        assertNotNull(user.getPosts());
    }

    @After
    public void dropTable() throws Exception {
        userRep.deleteAllInBatch();
    }

}