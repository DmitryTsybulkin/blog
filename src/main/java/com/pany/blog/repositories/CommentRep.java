package com.pany.blog.repositories;

import com.pany.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRep extends JpaRepository<Comment, Long> {
}
