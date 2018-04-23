package com.pany.blog;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
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

	private List<Post> posts = new ArrayList<>();

	private List<Comment> comments = new ArrayList<>();

	private User user;

	public void initDb() throws Exception {
		userRep.deleteAllInBatch();

		roles.add(roleRep.save(new Role("ADMIN")));
		user = userRep.save(new User("login", "password", roles, posts));

		Post post = postRep.save(new PostBuilder()
				.withUser(user)
				.withHeader("header")
				.withDateCreated(LocalDateTime.now())
				.withComments(null)
				.withContent("content")
				.build());
		comments.add(commentRep.save(new Comment(user.getLogin(), "text", LocalDateTime.now(), post)));
		comments.add(commentRep.save(new Comment(user.getLogin(), "newText", LocalDateTime.now(), post)));
	}

	@Test
	public void createPost() throws Exception {
		Post post = postRep.save(new PostBuilder().withHeader("header").withComments(comments).withContent("content").withDateCreated(LocalDateTime.now()).withUser(user).build());

		Optional<Post> targetPost = Optional.of(postRep.findPostByHeader("header").orElseThrow(EntityNotFoundException::new));

		assertEquals(targetPost.get().getHeader(), post.getHeader());
		assertEquals(targetPost.get().getContent(), post.getContent());
		assertEquals(targetPost.get().getComments(), post.getComments());
		assertEquals(targetPost.get().getDateCreated(), post.getDateCreated());
		assertEquals(targetPost.get().getId(), post.getId());
		assertEquals(targetPost.get().getUser().getId(), post.getUser().getId());
		assertEquals(targetPost.get().getUser().getLogin(), post.getUser().getLogin());
		assertNull(postRep.findPostByHeader("headerizer"));
	}

	@Test
	public void listPosts() throws Exception {
		Post post = postRep.save(new PostBuilder().withHeader("header").withComments(comments).withContent("content").withDateCreated(LocalDateTime.now()).withUser(user).build());
		Optional<Post> targetPost = Optional.of(postRep.findPostByHeader("header").orElseThrow(EntityNotFoundException::new));
		assert !postRep.findAll().isEmpty();
		assertEquals(targetPost.get().getUser().getLogin(), post.getUser().getLogin());
		assertEquals(targetPost.get().getId(), post.getId());
		assertEquals(targetPost.get().getDateCreated(), post.getDateCreated());
	}

	@Test
	public void updatePost() throws Exception {
		Post post = postRep.save(new PostBuilder().withHeader("header").withComments(comments).withContent("content").withDateCreated(LocalDateTime.now()).withUser(user).build());

		post.setHeader("newHeader");
		post.setDateCreated(LocalDateTime.now());

		Optional<Post> newPost = Optional.of(postRep.findPostByHeader("newHeader").orElseThrow(EntityNotFoundException::new));
		assertEquals(newPost.get().getHeader(), post.getHeader());
		assertEquals(newPost.get().getDateCreated(), post.getDateCreated());
	}

	@Test
	public void deletePost() throws Exception {
		Post post = postRep.save(new PostBuilder().withHeader("header").withComments(comments).withContent("content").withDateCreated(LocalDateTime.now()).withUser(user).build());

		postRep.deleteById(post.getId());

		assertTrue(postRep.findAll().isEmpty());
		assertNull(postRep.findPostByHeader("header"));
	}

	@After
	public void dropDb() throws Exception {
		postRep.deleteAllInBatch();
		commentRep.deleteAllInBatch();
		userRep.deleteAllInBatch();
	}

}
