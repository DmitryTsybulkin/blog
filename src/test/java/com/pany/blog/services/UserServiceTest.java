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

    @Autowired
    private UserRep userRep;

    @Autowired
    private RoleRep roleRep;

    @Autowired
    private PostRep postRep;

    private Set<Role> roles = new HashSet<>();

    private List<Post> posts = new ArrayList<>();

    private User user = new User();

    @Before
    public void initDb() throws Exception {
        roles.add(roleRep.save(new Role("USER_ROLE")));
        user = userRep.save(new User("login", "password", roles, posts));
        posts.add(postRep.save(new PostBuilder()
                .withHeader("header")
                .withContent("content")
                .withUser(user)
                .withComments(null)
                .withDateCreated(LocalDateTime.now())
                .build()));
    }

    @Test
    public void createUser() {
        UserDto dto = new UserDto();
        dto.login = "user";
        dto.email = "a@gmail.com";
        userService.createUser(dto);
        Optional<User> targetUser = userRep.findUserByLogin("user");
        assertNotNull(targetUser);
        assertTrue(targetUser.get().getRoles().containsAll(roles));
        assertNotNull(targetUser.get().getPassword());
        assertNotNull(targetUser.get().getId());
        assertNull(targetUser.get().getPosts());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createUserFailedEntryDuplicate() {
        UserDto dto = new UserDto();
        dto.login = "login";
        dto.email = "a@gmail.com";
        userService.createUser(dto);
    }

    @Test
    public void getUserById() {
        Long id = user.getId();
        UserDto dto = userService.getUserById(id);
        assertEquals(dto.id, user.getId());
        assertEquals(dto.login, user.getLogin());
        assertTrue(dto.roles.containsAll(user.getRoles()));
        assertTrue(dto.posts.containsAll(user.getPosts()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByIdFailedUserDoesNotExists() {
        Long id = 5L;
        userService.getUserById(id);
    }

    @Test
    public void getUsersList() {
        UserDto userDto = new UserDto();
        userDto.login = user.getLogin();
        userDto.id = user.getId();
        userDto.roles = user.getRoles();
        userDto.posts = user.getPosts();
        List<UserDto> dtos = userService.getUsersList();
        assert !dtos.isEmpty();
        assertTrue(dtos.contains(userDto));
        int ind = dtos.indexOf(userDto);
        assertEquals(dtos.get(ind).id, userDto.id);
        assertEquals(dtos.get(ind).login, userDto.login);
        assertTrue(dtos.get(ind).posts.containsAll(userDto.posts));
        assertTrue(dtos.get(ind).roles.containsAll(userDto.roles));
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
        UserDto dto = userService.toDto(user);
        assertEquals(dto.login, user.getLogin());
        assertEquals(dto.id, user.getId());
        assertTrue(dto.posts.containsAll(user.getPosts()));
        assertTrue(dto.roles.containsAll(user.getRoles()));
    }

    @Test
    public void fromDto() {
        UserDto dto = new UserDto();
        dto.posts = posts;
        dto.roles = roles;
        dto.login = this.user.getLogin();
        User targetUser = userService.fromDto(dto, user.getPassword());
        assertEquals(targetUser.getLogin(), dto.login);
        assertTrue(targetUser.getPosts().containsAll(dto.posts));
        assertTrue(targetUser.getRoles().containsAll(dto.roles));
    }
}