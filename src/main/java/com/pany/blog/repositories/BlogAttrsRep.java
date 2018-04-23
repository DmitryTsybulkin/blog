package com.pany.blog.repositories;

import com.pany.blog.model.BlogAttrs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogAttrsRep extends JpaRepository<BlogAttrs, Long> {
}
