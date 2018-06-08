package com.pany.blog.services;

import com.pany.blog.dtos.UserDto;
import com.pany.blog.exceptions.EntryDuplicateException;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRep userRep;

    @Autowired
    private RoleRep roleRep;

    @Before
    public void initRoles() throws Exception {
        roleRep.save(new Role("ADMIN"));
    }

    @Test
    public void createUser() throws Exception {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);
        UserDto targetUser = userService.getUserByLogin(login);
        assertNotNull(targetUser);
        assertNotNull(targetUser.id);
        assertEquals(targetUser.email, dto.email);
        assertEquals(targetUser.login, dto.login);
        assertFalse(targetUser.roles.isEmpty());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createUserFailedEntryDuplicate() throws Exception {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);

        UserDto anotherDto = new UserDto();
        anotherDto.email = "anotherEmail";
        anotherDto.login = login;
        userService.createUser(dto);
    }

    @Test
    public void getUserById() throws Exception {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);

        UserDto pointDto = userService.getUserByLogin(login);
        UserDto targetDto = userService.getUserById(pointDto.id);
        assertNotNull(targetDto);
        assertNotNull(targetDto.login);
        assertEquals(targetDto.login, login);
        assertEquals(targetDto.email, dto.email);
        assertFalse(targetDto.roles.isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByIdFailedUserDoesNotExists() throws Exception {
        Long id = 5000L;
        userService.getUserById(id);
    }

    @Test
    public void getUsersList() throws Exception {
        UserDto dto = new UserDto();
        dto.email = "email";
        dto.login = "login";
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);

        UserDto anotherDto = new UserDto();
        anotherDto.email = "anotherEmail";
        anotherDto.login = "anotherLogin";
        anotherDto.password = "anotherPassword";
        anotherDto.roles = new ArrayList<>();
        anotherDto.roles.add("ADMIN");
        userService.createUser(anotherDto);

        List<UserDto> dtos = userService.getUsersList();
        for (UserDto userDto : dtos) {
            assertNotNull(userDto.id);
            assertFalse(userDto.roles.isEmpty());
        }
    }

    @Test
    public void updateUser() throws Exception {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        dto.email = "mail";
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);

        Long id = userService.getUserByLogin(login).id;
        UserDto anotherDto = new UserDto();
        anotherDto.login = "topLogin";
        anotherDto.email = "anotherMail";
        anotherDto.id = id;
        anotherDto.roles = new ArrayList<String>();
        anotherDto.roles.add("ADMIN");
        userService.updateUser(anotherDto);

        UserDto targetDto = userService.getUserById(id);
        assertEquals(targetDto.login, anotherDto.login);
        assertEquals(targetDto.email, anotherDto.email);
        assertFalse(targetDto.roles.isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUser() throws Exception {
        UserDto dto = new UserDto();
        String login = "login";
        dto.email = "email";
        dto.login = login;
        dto.password = "password";
        dto.roles = new ArrayList<>();
        dto.roles.add("ADMIN");
        userService.createUser(dto);

        Long id = userService.getUserByLogin(login).id;
        userService.deleteUser(id);

        assertNull(userService.getUserById(id));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUserFailedUserDoesNotExists() throws Exception {
        userService.deleteUser(5000L);
    }

    @Test
    public void toDto() throws Exception {
        User user = new User("login", "password", "email", new HashSet<>(), new ArrayList<>());
        UserDto dto = userService.toDto(user);
        assertEquals(dto.login, user.getLogin());
        assertEquals(dto.email, user.getEmail());
        assertNotNull(dto.roles);
    }

    @Test
    public void fromDto() throws Exception {
        UserDto dto = new UserDto();
        dto.login = "login";
        dto.email = "email";
        dto.password = "password";
        dto.roles = new ArrayList<String>();
        dto.roles.add("ADMIN");
        User user = userService.fromDto(dto);
        assertEquals(user.getLogin(), dto.login);
        assertEquals(user.getEmail(), dto.email);
        assertFalse(user.getRoles().isEmpty());
        assertEquals(user.getPassword(), "password");
    }

    @After
    public void dropTable() throws Exception {
        userRep.deleteAllInBatch();
        roleRep.deleteAllInBatch();
    }

}