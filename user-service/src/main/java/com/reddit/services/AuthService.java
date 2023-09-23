package com.reddit.services;

import com.reddit.constant.MessageConstant;
import com.reddit.dao.UserDao;
import com.reddit.dtos.RedisUserDTO;
import com.reddit.dtos.SignInDTO;
import com.reddit.dtos.SignUpDTO;
import com.reddit.entities.Role;
import com.reddit.entities.User;
import com.reddit.entities.VerificationToken;
import com.reddit.enums.RoleType;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.repositories.RoleRepository;
import com.reddit.repositories.UserRepository;
import com.reddit.repositories.VerificationTokenRepository;
import com.reddit.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailService emailService;

    private final RoleRepository roleRepository;

    private final JwtUtil jwtUtil;

    private final VerificationTokenService verificationTokenService;

    private final UserDao userDao;

    private final ModelMapper modelMapper;
    

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository,
                       EmailService emailService,
                       RoleRepository roleRepository,
                       JwtUtil jwtUtil,
                       VerificationTokenService verificationTokenService,
                       UserDao userDao,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.verificationTokenService = verificationTokenService;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }

    public void signUp(SignUpDTO signUpDTO) {
        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        user.setEnabled(false);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(RoleType.ROLE_USER);
        if (role != null) {
            roles.add(role);
        }
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        String token = verificationTokenService.generateVerificationToken(savedUser);
        emailService.sendAccountVerifyEmail(savedUser.getEmail(), token, savedUser.getUsername());
    }

    public String accountVerification(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null) {
            return verificationTokenService.isVerificationTokenExpired(verificationToken);
        }
        return MessageConstant.INVALID_VERIFICATION_TOKEN;
    }


    public Object login(SignInDTO signInDTO) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Map<String, Object> mapLoginRespData = new HashMap<>();
        User user = userRepository.findByUsername(signInDTO.getUsername()).orElseThrow(() -> new SpringRedditException("User not found with this username"));
        String isVerified = verificationTokenService.accountNotVerifiedYet(user);
        if (isVerified.equals(MessageConstant.ACCOUNT_VERIFY_SUCCESS)) {
            boolean matches = passwordEncoder.matches(signInDTO.getPassword(), user.getPassword());
            if (matches) {
                String token = jwtUtil.getToken(user.getId(), user.getRoles().stream().map(Role::getName).collect(Collectors.toList()), user.getEmail());
                RedisUserDTO mapRedisUser = modelMapper.map(user, RedisUserDTO.class);
                mapRedisUser.setRoleName(user.getRoles().stream().map(role -> role.getName().name()).findFirst().orElse(null));
                RedisUserDTO getUserFromRedis = userDao.getUserByUserId(user.getId());
                if (getUserFromRedis == null) {
                    getUserFromRedis = userDao.saveUserOnRedis(mapRedisUser);
                }
                mapLoginRespData.put("user", getUserFromRedis);
                mapLoginRespData.put("token", token);
                return mapLoginRespData;
            } else {
                return MessageConstant.IN_CORRECT_PASSWORD;
            }
        }
        return isVerified;
    }


}
