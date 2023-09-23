package com.reddit.utils;

import com.reddit.constant.MessageConstant;
import com.reddit.entities.User;
import com.reddit.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class CommonUtil {

    private final HttpServletRequest httpServletRequest;

    private final ResponseHandler responseHandler;

    private final UserRepository userRepository;

    @Autowired
    public CommonUtil(HttpServletRequest httpServletRequest,
                      ResponseHandler responseHandler,
                      UserRepository userRepository) {
        this.httpServletRequest = httpServletRequest;
        this.responseHandler = responseHandler;
        this.userRepository=userRepository;
    }

    public ResponseEntity<Object> notASameUser(User user) {
        long id = Long.parseLong(httpServletRequest.getHeader("id"));
        if (id != 0L && id != user.getId()) {
            return responseHandler.response("", MessageConstant.ACCESS_DENIED, false, HttpStatus.BAD_GATEWAY);
        }
        return null;
    }

    public ResponseEntity<Object> requestValidation(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorMessage.append(error.getDefaultMessage()).append(". ");
        }
        log.info(MessageConstant.REQUEST_ERROR, errorMessage);
        return responseHandler.response("", errorMessage.toString(), false,
                HttpStatus.BAD_REQUEST);
    }


    public User getCurrentUser() {
        long userId = Long.parseLong(httpServletRequest.getHeader("id"));

        return userRepository.findById(userId).orElse(null);
    }

}
