package com.reddit.controllers;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.VoteDto;
import com.reddit.services.VoteService;
import com.reddit.utils.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    private final ResponseHandler responseHandler;

    @Autowired
    public VoteController(VoteService voteService,
                          ResponseHandler responseHandler) {
        this.voteService = voteService;
        this.responseHandler = responseHandler;
    }

    @PostMapping("/vote")
    public ResponseEntity<Object> vote(@RequestBody VoteDto voteDto) {
        try {
            log.info("Vote request received by : {}", voteDto);
            voteService.vote(voteDto);
            return responseHandler.response("", MessageConstant.VOTE_SUCCESS, true, HttpStatus.OK);
        } catch (Exception e) {
            log.error(MessageConstant.VOTE_ERROR, e);
        }
        return responseHandler.response("", MessageConstant.VOTE_ERROR, false, HttpStatus.BAD_REQUEST);
    }

}
