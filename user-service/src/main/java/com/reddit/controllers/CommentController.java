package com.reddit.controllers;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CommentDTO;
import com.reddit.dtos.CreateCommentDTO;
import com.reddit.services.CommentService;
import com.reddit.utils.CommonUtil;
import com.reddit.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    private final ResponseHandler responseHandler;

    private final CommonUtil commonUtil;

    @Autowired
    public CommentController(CommentService commentService,
                             ResponseHandler responseHandler,
                             CommonUtil commonUtil) {
        this.commentService = commentService;
        this.responseHandler = responseHandler;
        this.commonUtil = commonUtil;
    }

    @PostMapping("/createComment")
    public ResponseEntity<Object> createComment(@Validated @RequestBody CreateCommentDTO createCommentDTO, BindingResult bindingResult) {
        try {
            log.info("Create comment request received by : {}", createCommentDTO);
            if (bindingResult.hasErrors()) {
                return commonUtil.requestValidation(bindingResult);
            }
            Object commentObj = commentService.createComment(createCommentDTO, commonUtil.getCurrentUser());
            if (commentObj != null && commentObj.equals(MessageConstant.USER_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.USER_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            } else if (commentObj != null && commentObj.equals(MessageConstant.POST_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.POST_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            }
            return responseHandler.response(commentObj, MessageConstant.COMMENT_CREATED_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.COMMENT_CREATED_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.COMMENT_CREATED_ERROR, false, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/getAllCommentsForPost/{postId}")
    public ResponseEntity<Object> getAllCommentsForPost(@PathVariable(name = "postId") Long postId) {
        try {
            log.info("Get all comments for post request received by PostId : {}", postId);
            List<CommentDTO> allCommentsForPost = commentService.getAllCommentsForPost(postId);
            if (!allCommentsForPost.isEmpty())
                return responseHandler.response(allCommentsForPost, MessageConstant.ALL_COMMENT_FOR_POST_SUCCESS, true, HttpStatus.OK);
            else
                return responseHandler.response("", MessageConstant.COMMENTS_NOT_FOUND, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.ALL_COMMENT_FOR_POST_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.ALL_COMMENT_FOR_POST_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAllCommentsForUser/{username}")
    public ResponseEntity<Object> getAllCommentsForUser(@PathVariable(name = "username") String username) {
        try {
            log.info("Get all comments for user request received by username : {}", username);
            List<CommentDTO> allCommentsForUser = commentService.getAllCommentsForUser(username);
            if (!allCommentsForUser.isEmpty())
                return responseHandler.response(allCommentsForUser, MessageConstant.ALL_COMMENT_FOR_USER_SUCCESS, true, HttpStatus.OK);
            else
                return responseHandler.response("", MessageConstant.COMMENTS_NOT_FOUND, false, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(MessageConstant.ALL_COMMENT_FOR_USER_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.ALL_COMMENT_FOR_USER_ERROR, false, HttpStatus.BAD_REQUEST);
    }

}
