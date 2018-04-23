package com.pany.blog.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blog_attributes")
public class BlogAttrs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long key;

	@Column(name = "name")
	private String value;

	@Column(name = "description")
	private String description;

	public BlogAttrs() {
	}

	public BlogAttrs(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
