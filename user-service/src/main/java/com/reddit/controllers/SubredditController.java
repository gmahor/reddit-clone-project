package com.reddit.controllers;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CreateSubredditDTO;
import com.reddit.dtos.SubredditDTO;
import com.reddit.services.SubredditService;
import com.reddit.utils.CommonUtil;
import com.reddit.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/Subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    private final ResponseHandler responseHandler;

    private final CommonUtil commonUtil;

    @Autowired
    public SubredditController(SubredditService subredditService,
                               ResponseHandler responseHandler,
                               CommonUtil commonUtil) {
        this.subredditService = subredditService;
        this.responseHandler = responseHandler;
        this.commonUtil = commonUtil;
    }


    @PostMapping(value = "/createSubreddit")
    public ResponseEntity<Object> createSubreddit(@RequestBody CreateSubredditDTO createSubredditDTO, BindingResult bindingResult) {
        try {
            log.info("Creating subreddit request received : {}", createSubredditDTO);
            if (bindingResult.hasErrors()) {
                return commonUtil.requestValidation(bindingResult);
            }
            Object subredditObj = subredditService.createSubreddit(createSubredditDTO);
            if (subredditObj != null && subredditObj.equals(MessageConstant.SUB_REDDIT_NAME_ALREADY_PRESENT)) {
                return responseHandler.response("", MessageConstant.SUB_REDDIT_NAME_ALREADY_PRESENT, false, HttpStatus.BAD_REQUEST);
            }
            return responseHandler.response(subredditObj, MessageConstant.SUB_REDDIT_CREATED_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.SUB_REDDIT_CREATED_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.SUB_REDDIT_CREATED_ERROR, false, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/getAllSubreddits")
    public ResponseEntity<Object> getAllSubreddits() {
        try {
            log.info("Get all subreddit request received");
            List<SubredditDTO> subredditDTOs = subredditService.getAllSubreddits();
            if (!subredditDTOs.isEmpty()) {
                return responseHandler.response(subredditDTOs, MessageConstant.ALL_SUB_REDDIT_SUCCESS, true, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(MessageConstant.ALL_SUB_REDDIT_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.ALL_SUB_REDDIT_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getAllSubreddit(@PathVariable(name = "id") long id) {
        try {
            log.info("Get subreddit request received by id : {}", id);
            Object subredditObj = subredditService.getSubreddit(id);
            if (subredditObj != null && subredditObj.equals(MessageConstant.SUB_REDDIT_NOT_FOUND)) {
                return responseHandler.response("", subredditObj.toString(), false, HttpStatus.BAD_REQUEST);
            }
            return responseHandler.response(subredditObj, MessageConstant.SUB_REDDIT_FETCHED_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.ALL_SUB_REDDIT_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.ALL_SUB_REDDIT_ERROR, false, HttpStatus.BAD_REQUEST);
    }

}
