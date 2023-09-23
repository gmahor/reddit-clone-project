package com.reddit.repositories;

import com.reddit.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findFirstByUserIdOrderByIdDesc(Long userId);


    List<VerificationToken> findAllByUserId(Long userId);
}
