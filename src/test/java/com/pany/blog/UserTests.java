package com.pany.blog;

import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Post;
import com.pany.blog.model.PostBuilder;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
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
public class UserTests {

	@Autowired
	private UserRep userRep;

	@Autowired
	private PostRep postRep;

	@Autowired
	private RoleRep roleRep;

	private Set<Role> roles = new HashSet<>();

	private List<Post> posts = new ArrayList<>();

	public void initTable() throws Exception {
		userRep.deleteAllInBatch();
		roleRep.deleteAllInBatch();

		roles.add(roleRep.save(new Role("ADMIN")));
		posts.add(postRep.save(new PostBuilder()
				.withContent("content")
				.withComments(null)
				.withDateCreated(LocalDateTime.now())
				.withHeader("header")
				.withUser(null).build()));
		posts.add(postRep.save(new PostBuilder()
				.withContent("content2")
				.withComments(null)
				.withDateCreated(LocalDateTime.now())
				.withHeader("header2")
				.withUser(null).build()));
	}

	@Test
	public void saveUser() throws Exception {
		User user = userRep.save(new User("login", "password", roles, posts));

		assertEquals(userRep.findById(user.getId()).orElseThrow(ResourceNotFoundException::new), user);

		Optional<User> targetUser = Optional.of(userRep.findUserByLogin("login").orElseThrow(ResourceNotFoundException::new));

		assertEquals(targetUser.get().getLogin(), user.getLogin());
		assertEquals(targetUser.get().getRoles(), user.getRoles());
		assertEquals(targetUser.get().getId(), user.getId());
		assertTrue(targetUser.get().getPosts().containsAll(user.getPosts()));

		dropUsers();
	}

	@Test
	public void listUsers() throws Exception {
		userRep.save(new User("login", "password", roles, posts));

		assert !userRep.findAll().isEmpty();

		dropUsers();
	}

	@Test
	public void updateUser() throws Exception {
		User user = userRep.save(new User("loginizer", "badPassword", roles, posts));

		Optional<User> targetUser = Optional.of(userRep.findUserByLogin("loginizer").orElseThrow(EntityNotFoundException::new));

		assertEquals(targetUser.get().getLogin(), user.getLogin());

		targetUser.get().setLogin("login");
		targetUser.get().setPassword("goodPassword");

		dropUsers();
	}

	@Test
	public void deleteUser() throws Exception {
		User user = userRep.save(new User("loginizer", "badPassword", roles, posts));

		userRep.deleteById(user.getId());

		assertTrue(userRep.findAll().isEmpty());
	}

	private void dropUsers() {
		userRep.deleteAllInBatch();
	}

	@After
	public void dropTable() throws Exception {
		userRep.deleteAllInBatch();
		roleRep.deleteAllInBatch();
	}

}
