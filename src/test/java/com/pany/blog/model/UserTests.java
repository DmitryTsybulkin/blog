package com.pany.blog.model;

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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTests {

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
        roles.add(roleRep.save(new Role("ADMIN")));
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
    public void createUser() throws Exception {
        User noob = userRep.save(new User("log", "pass", roles, posts));
        Optional<User> targetUser = Optional.of(userRep.findUserByLogin("log").orElseThrow(ResourceNotFoundException::new));
        assertEquals(targetUser.get().getId(), noob.getId());
        assertEquals(targetUser.get().getLogin(), noob.getLogin());
        assertTrue(targetUser.get().getRoles().containsAll(noob.getRoles()));
        assertTrue(targetUser.get().getPosts().containsAll(noob.getPosts()));
        userRep.delete(noob);
    }

    @Test
    public void listUsers() throws Exception {
        List<User> users = userRep.findAll();
        assertFalse(users.isEmpty());
        Optional<User> targetUser = userRep.findUserByLogin(user.getLogin());
        assertEquals(targetUser.get().getId(), user.getId());
        assertEquals(targetUser.get().getLogin(), user.getLogin());
        assertTrue(targetUser.get().getPosts().containsAll(user.getPosts()));
        assertTrue(targetUser.get().getRoles().containsAll(user.getRoles()));
    }

    @Test
    public void updateUser() throws Exception {
        user.setLogin("loginizer");
        User targetUser = userRep.getOne(user.getId());
        assertEquals(targetUser.getLogin(), "loginizer");
        assertTrue(targetUser.getRoles().containsAll(user.getRoles()));
        assertTrue(targetUser.getPosts().containsAll(user.getPosts()));
    }

    @Test
    public void deleteUser() throws Exception {
        Long id = user.getId();
        postRep.deleteAll(postRep.findPostsByUser_Id(id));
        userRep.deleteById(id);
        assertFalse(userRep.existsById(id));
    }

}
