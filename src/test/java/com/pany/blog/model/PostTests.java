package com.pany.blog.model;

import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Comment;
import com.pany.blog.model.Post;
import com.pany.blog.model.PostBuilder;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.CommentRep;
import com.pany.blog.repositories.PostRep;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import org.junit.After;
import org.junit.Before;
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
public class PostTests {


    @Autowired
    private UserRep userRep;

    @Autowired
    private PostRep postRep;

    @Autowired
    private RoleRep roleRep;

    @Autowired
    private CommentRep commentRep;

    private Set<Role> roles = new HashSet<>();

    private List<Comment> comments = new ArrayList<>();

    private List<Post> posts = new ArrayList<>();

    private User user = new User();

    private Post post;

    @Before
    public void initDb() throws Exception {
        roles.add(roleRep.save(new Role("ADMIN")));
        user = userRep.save(new User("login", "password", roles, null));
        post = postRep.save(new PostBuilder()
                .withHeader("header")
                .withContent("content")
                .withUser(user)
                .withComments(null)
                .withDateCreated(LocalDateTime.now())
                .build());
        posts.add(post);
        user.setPosts(posts);
        comments.add(new Comment(user.getLogin(), "text", LocalDateTime.now(), post));
        comments.add(new Comment(user.getLogin(), "one_more_text", LocalDateTime.now(), post));
    }

    @Test
    public void createPost() throws Exception {
        Post best_post = postRep.save(new PostBuilder()
                .withHeader("head")
                .withContent("cont")
                .withComments(null)
                .withDateCreated(LocalDateTime.now())
                .withUser(user)
                .build());
        Optional<Post> targetPost = Optional.of(postRep.findPostByHeader(best_post.getHeader()).orElseThrow(ResourceNotFoundException::new));
        assertEquals(targetPost.get().getContent(), best_post.getContent());
        assertEquals(targetPost.get().getUser().getLogin(), best_post.getUser().getLogin());
        assertEquals(targetPost.get().getUser().getId(), best_post.getUser().getId());
        assertEquals(targetPost.get().getId(), best_post.getId());
        assertEquals(targetPost.get().getDateCreated(), best_post.getDateCreated());
        assertNull(targetPost.get().getComments());
    }

    @Test
    public void listPosts() throws Exception {
        List<Post> postes = postRep.findAll();
        assertFalse(postes.isEmpty());
        assertTrue(postes.containsAll(posts));
        Post best_post = postes.get(0);
        Post post = posts.get(0);
        assertEquals(best_post.getId(), post.getId());
        assertEquals(best_post.getUser().getId(), post.getUser().getId());
        assertEquals(best_post.getUser().getLogin(), post.getUser().getLogin());
        assertEquals(best_post.getDateCreated(), post.getDateCreated());
        assertEquals(best_post.getHeader(), post.getHeader());
        assertEquals(best_post.getContent(), post.getContent());
    }

    @Test
    public void updatePost() throws Exception {
        Post post = posts.get(0);
        post.setHeader("newHeader");
        post.setContent("contt");
        Post targetPost = postRep.getOne(post.getId());
        assertEquals(targetPost.getHeader(), "newHeader");
        assertEquals(targetPost.getContent(), "contt");
        assertEquals(targetPost.getDateCreated(), post.getDateCreated());
        assertEquals(targetPost.getUser().getId(), post.getUser().getId());
        assertEquals(targetPost.getUser().getLogin(), post.getUser().getLogin());
    }

    @Test
    public void deletePost() throws Exception {
        Long id = post.getId();
        user.setPosts(null);
        postRep.deleteById(id);
        assertFalse(postRep.existsById(id));
    }

}
