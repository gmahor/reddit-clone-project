package com.reddit.services;

import com.reddit.constant.MessageConstant;
import com.reddit.entities.User;
import com.reddit.entities.VerificationToken;
import com.reddit.repositories.UserRepository;
import com.reddit.repositories.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailService emailService;

    private final UserRepository userRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository,
                                    EmailService emailService,
                                    UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }


    String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusSeconds(300));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    String accountNotVerifiedYet(User user) {
        if (!user.isEnabled()) {
            VerificationToken verificationToken = verificationTokenRepository.findFirstByUserIdOrderByIdDesc(user.getId());
            String isTokenExpired = this.isVerificationTokenExpired(verificationToken);
            if (isTokenExpired.equals(MessageConstant.VERIFICATION_TOKEN_EXPIRED)) {
                String token = this.generateVerificationToken(user);
                emailService.sendAccountVerifyEmail(user.getEmail(), token, user.getUsername());
                return MessageConstant.ACCOUNT_NOT_VERIFY;
            }
        }
        return MessageConstant.ACCOUNT_VERIFY_SUCCESS;
    }


    String isVerificationTokenExpired(VerificationToken verificationToken) {
        if (verificationToken.getExpiryDate().isAfter(Instant.now())) {
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            log.info("User verified!!");
            deleteUserVerificationToken(user.getId());
            return MessageConstant.ACCOUNT_VERIFY_SUCCESS;
        } else {
            return MessageConstant.VERIFICATION_TOKEN_EXPIRED;
        }
    }

    @Transactional
    void deleteUserVerificationToken(long userId) {
        List<VerificationToken> verificationTokens = verificationTokenRepository.findAllByUserId(userId);
        verificationTokenRepository.deleteAll(verificationTokens);
        log.info("Verification tokens deleted by userId : {}", userId);
    }
}
