package com.pany.blog.repositories;

import com.pany.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRep extends JpaRepository<Post, Long> {
    Optional<Post> findPostByHeader(String header);
    List<Post> findPostsByUser_Id(Long id);
}
