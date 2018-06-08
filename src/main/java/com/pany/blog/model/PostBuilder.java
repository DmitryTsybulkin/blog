package com.pany.blog.model;

import java.time.LocalDateTime;
import java.util.List;

public final class PostBuilder {
	private String header;
	private String content;
	private LocalDateTime dateCreated;
	private User user;
	private List<Comment> comments;

	public PostBuilder() {
	}

	public static PostBuilder aPost() {
		return new PostBuilder();
	}

	public PostBuilder withHeader(String header) {
		this.header = header;
		return this;
	}

	public PostBuilder withContent(String content) {
		this.content = content;
		return this;
	}

	public PostBuilder withDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	public PostBuilder withUser(User user) {
		this.user = user;
		return this;
	}

	public PostBuilder withComments(List<Comment> comments) {
		this.comments = comments;
		return this;
	}

	public Post build() {
		Post post = new Post();
		post.setHeader(header);
		post.setContent(content);
		post.setDateCreated(dateCreated);
		post.setUser(user);
		post.setComments(comments);
		return post;
	}
}
