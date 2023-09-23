package com.reddit.repositories;

import com.reddit.entities.Comment;
import com.reddit.entities.Post;
import com.reddit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
