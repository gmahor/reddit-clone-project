package com.reddit.controllers;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CreatePostDTO;
import com.reddit.dtos.PostDTO;
import com.reddit.services.PostService;
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
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    private final ResponseHandler responseHandler;

    private final CommonUtil commonUtil;

    @Autowired
    public PostController(PostService postService,
                          ResponseHandler responseHandler,
                          CommonUtil commonUtil) {
        this.postService = postService;
        this.responseHandler = responseHandler;
        this.commonUtil = commonUtil;
    }

    @PostMapping("/createPost")
    public ResponseEntity<Object> createPost(@Validated @RequestBody CreatePostDTO createPostDTO, BindingResult bindingResult) {
        try {
            log.info("Create post request received by : {}", createPostDTO);
            if (bindingResult.hasErrors()) {
                return commonUtil.requestValidation(bindingResult);
            }
            Object postObj = postService.createPost(createPostDTO, commonUtil.getCurrentUser());
            if (postObj != null && postObj.equals(MessageConstant.SUB_REDDIT_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.SUB_REDDIT_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            } else if (postObj != null && postObj.equals(MessageConstant.USER_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.USER_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            }
            return responseHandler.response(postObj, MessageConstant.POST_CREATED_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.POST_CREATED_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.POST_CREATED_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<Object> getPost(@PathVariable(name = "id") Long id) {
        try {
            log.info("Get post request received by id : {}", id);
            PostDTO postDTO = postService.getPost(id);
            if (postDTO != null) {
                return responseHandler.response(postDTO, MessageConstant.POST_FETCHED_SUCCESS, true, HttpStatus.OK);
            }
            return responseHandler.response("", MessageConstant.POST_NOT_FOUND, false, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(MessageConstant.POST_FETCHED_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.POST_FETCHED_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<Object> getAllPosts() {
        try {
            log.info("Get all posts request received");
            List<PostDTO> allPosts = postService.getAllPosts();
            if (!allPosts.isEmpty()) {
                return responseHandler.response(allPosts, MessageConstant.POSTS_FETCHED_SUCCESS, true, HttpStatus.OK);
            }
            return responseHandler.response("", MessageConstant.POST_NOT_FOUND, false, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(MessageConstant.POSTS_FETCHED_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.POSTS_FETCHED_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getPostsBySubreddit/{subredditId}")
    public ResponseEntity<Object> getPostsBySubreddit(@PathVariable(name = "subredditId") Long subredditId) {
        try {
            log.info("Get all posts by subreddit request received by subredditId : {}", subredditId);
            Object postsBySubreddit = postService.getPostsBySubreddit(subredditId);
            if (postsBySubreddit != null && postsBySubreddit.equals(MessageConstant.SUB_REDDIT_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.SUB_REDDIT_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            }
            return responseHandler.response(postsBySubreddit, MessageConstant.SUB_REDDIT_POSTS_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.SUB_REDDIT_POSTS_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.SUB_REDDIT_POSTS_ERROR, false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getPostsByUsername/{username}")
    public ResponseEntity<Object> getPostsByUsername(@PathVariable(name = "username") String username) {
        try {
            log.info("Get all posts by username request received by username : {}", username);
            Object postsByUsername = postService.getPostsByUsername(username);
            if (postsByUsername != null && postsByUsername.equals(MessageConstant.USER_NOT_FOUND)) {
                return responseHandler.response("", MessageConstant.USER_NOT_FOUND, false, HttpStatus.NOT_FOUND);
            }
            return responseHandler.response(postsByUsername, MessageConstant.USERNAME_POSTS_FETCHED_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.USERNAME_POSTS_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.USERNAME_POSTS_ERROR, false, HttpStatus.BAD_REQUEST);
    }


}
