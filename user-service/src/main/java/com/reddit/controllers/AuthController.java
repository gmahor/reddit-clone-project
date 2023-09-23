package com.reddit.controllers;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.SignInDTO;
import com.reddit.dtos.SignUpDTO;
import com.reddit.services.AuthService;
import com.reddit.utils.CommonUtil;
import com.reddit.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final ResponseHandler responseHandler;

    private final CommonUtil commonUtil;


    @Autowired
    public AuthController(ResponseHandler responseHandler,
                          AuthService authService,
                          CommonUtil commonUtil) {
        this.responseHandler = responseHandler;
        this.authService = authService;
        this.commonUtil = commonUtil;
        ;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) {
        try {
            log.info("Signup request received by : {}", signUpDTO);
            if (bindingResult.hasErrors()) {
                this.commonUtil.requestValidation(bindingResult);
            }
            authService.signUp(signUpDTO);
            return responseHandler.response("", MessageConstant.SIGN_UP_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.ERROR_SIGN_IN, e);
        }
        return responseHandler.response("", MessageConstant.ERROR_SIGN_UP, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/accountVerification/{token}")
    public ResponseEntity<Object> accountVerification(@PathVariable(name = "token") String token) {
        try {
            log.info("Account verification request received by token : {}", token);
            String verificationMsg = authService.accountVerification(token);
            switch (verificationMsg) {
                case MessageConstant.ACCOUNT_VERIFY_SUCCESS:
                    return responseHandler.response("", MessageConstant.ACCOUNT_VERIFY_SUCCESS, true, HttpStatus.OK);
                case MessageConstant.VERIFICATION_TOKEN_EXPIRED:
                    return responseHandler.response("", MessageConstant.VERIFICATION_TOKEN_EXPIRED, false, HttpStatus.BAD_REQUEST);
                case MessageConstant.INVALID_VERIFICATION_TOKEN:
                    return responseHandler.response("", MessageConstant.INVALID_VERIFICATION_TOKEN, false, HttpStatus.BAD_REQUEST);
                default:
                    break;
            }
        } catch (Exception e) {
            log.info(MessageConstant.ACCOUNT_VERIFY_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.ACCOUNT_VERIFY_ERROR, false, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody SignInDTO signInDTO, BindingResult bindingResult) {
        try {
            log.info("Signup request received by : {}", signInDTO);
            if (bindingResult.hasErrors()) {
                this.commonUtil.requestValidation(bindingResult);
            }
            Object loginObj = authService.login(signInDTO);
            if (loginObj != null && loginObj.equals(MessageConstant.IN_CORRECT_PASSWORD)) {
                return responseHandler.response("", MessageConstant.IN_CORRECT_PASSWORD, false, HttpStatus.BAD_REQUEST);
            } else if (loginObj != null && loginObj.equals(MessageConstant.ACCOUNT_NOT_VERIFY)) {
                return responseHandler.response("", MessageConstant.ACCOUNT_NOT_VERIFY, false, HttpStatus.BAD_REQUEST);
            }
            return responseHandler.response("", MessageConstant.SIGN_IN_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.ERROR_SIGN_IN, e);
        }
        return responseHandler.response("", MessageConstant.ERROR_SIGN_IN, false, HttpStatus.BAD_REQUEST);
    }


}
