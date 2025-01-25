package com.example.momowas.comment.repository;

import com.example.momowas.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsById(Long id);
}
