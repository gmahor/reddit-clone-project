package com.reddit.repositories;

import com.reddit.entities.Post;
import com.reddit.entities.User;
import com.reddit.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);

}
