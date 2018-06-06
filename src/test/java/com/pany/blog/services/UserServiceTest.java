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

    @Before
    public void initDb() throws Exception {
    }

    @Test
    public void createUser() {
        UserDto dto = new UserDto();
        String login = "login";
        dto.posts = null;
        dto.email = "email";
        dto.login = login;
        userService.createUser(dto);
        UserDto targetUser = userService.getUserByLogin(login);
        assertNotNull(targetUser);
        assertNotNull(targetUser.id);
        assertNotNull(targetUser.roles);
        assert targetUser.roles.size() == 1;
    }

    @Test(expected = EntryDuplicateException.class)
    public void createUserFailedEntryDuplicate() {
    }

    @Test
    public void getUserById() {
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByIdFailedUserDoesNotExists() {
        Long id = 5L;
        userService.getUserById(id);
    }

    @Test
    public void getUsersList() {
    }

    @Test
    public void updateUser() {

    }

    @Test
    public void deleteUser() {
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUserFailedUserDoesNotExists() {
        userService.deleteUser(5L);
    }

    @Test
    public void toDto() {
    }

    @Test
    public void fromDto() {
        UserDto dto = new UserDto();
    }
}