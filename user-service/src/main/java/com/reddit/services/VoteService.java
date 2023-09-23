package com.reddit.services;

import com.reddit.dtos.VoteDto;
import com.reddit.entities.Post;
import com.reddit.entities.Vote;
import com.reddit.enums.VoteType;
import com.reddit.exceptions.SpringRedditException;
import com.reddit.repositories.PostRepository;
import com.reddit.repositories.VoteRepository;
import com.reddit.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final PostRepository postRepository;
    
    private final CommonUtil commonUtil;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       CommonUtil commonUtil){
        this.voteRepository=voteRepository;
        this.postRepository=postRepository;
        this.commonUtil=commonUtil;
    }

    public void vote(VoteDto voteDto){
        Optional<Post> optionalPost = postRepository.findById(voteDto.getPostId());
        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            Optional<Vote> voteByPostAndUser  = voteRepository.findTopByPostAndUserOrderByIdDesc(post, commonUtil.getCurrentUser());
            if (voteByPostAndUser.isPresent() &&
                    voteByPostAndUser.get().getVoteType()
                            .equals(voteDto.getVoteType())) {
                throw new SpringRedditException("You have already "
                        + voteDto.getVoteType() + "'d for this post");
            }
            if (voteDto.getVoteType().equals(VoteType.UP_VOTE)) {
                post.setVoteCount(post.getVoteCount() + 1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
            Vote vote = new Vote();
            vote.setVoteType(voteDto.getVoteType());
            vote.setPost(post);
            vote.setUser(commonUtil.getCurrentUser());
            voteRepository.save(vote);
            postRepository.save(post);
        }
    }



}
