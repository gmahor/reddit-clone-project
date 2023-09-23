package com.reddit.services;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CreatePostDTO;
import com.reddit.dtos.PostDTO;
import com.reddit.entities.Post;
import com.reddit.entities.Subreddit;
import com.reddit.entities.User;
import com.reddit.repositories.PostRepository;
import com.reddit.repositories.SubredditRepository;
import com.reddit.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;

    private final SubredditRepository subredditRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       SubredditRepository subredditService,
                       ModelMapper modelMapper,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Object createPost(CreatePostDTO createPostDTO, User user) {
        Optional<Subreddit> optionalSubreddit = subredditRepository.findById(createPostDTO.getSubredditId());
        if (optionalSubreddit.isPresent()) {
            if (user != null) {
                Post post = new Post();
                post.setPostName(createPostDTO.getPostName());
                post.setUrl(createPostDTO.getUrl());
                post.setDescription(createPostDTO.getDescription());
                post.setSubreddit(optionalSubreddit.get());
                post.setUser(user);
                post.setVoteCount(0);
                Post savedPost = postRepository.save(post);
                PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);
                postDTO.setSubredditName(optionalSubreddit.get().getName());
                postDTO.setUserName(user.getUsername());
                return postDTO;
            }
            return MessageConstant.USER_NOT_FOUND;
        }
        return MessageConstant.SUB_REDDIT_NOT_FOUND;
    }

    public PostDTO getPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            PostDTO postDTO = modelMapper.map(optionalPost.get(), PostDTO.class);
            postDTO.setSubredditName(optionalPost.get().getSubreddit().getName());
            postDTO.setUserName(optionalPost.get().getUser().getUsername());
            return postDTO;
        }
        return null;
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(post -> {
            PostDTO postDTO = modelMapper.map(post, PostDTO.class);
            postDTO.setSubredditName(post.getSubreddit().getName());
            postDTO.setUserName(post.getUser().getUsername());
            return postDTO;
        }).collect(Collectors.toList());
    }

    public Object getPostsBySubreddit(Long subredditId) {
        Optional<Subreddit> optionalSubreddit = subredditRepository.findById(subredditId);
        if (optionalSubreddit.isPresent()) {
            return postRepository.findAllBySubreddit(optionalSubreddit.get()).stream().map(post -> {
                PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                postDTO.setSubredditName(post.getSubreddit().getName());
                postDTO.setUserName(post.getUser().getUsername());
                return postDTO;
            }).collect(Collectors.toList());
        }
        return MessageConstant.SUB_REDDIT_NOT_FOUND;
    }

    public Object getPostsByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return postRepository.findByUserId(optionalUser.get().getId()).stream().map(post -> {
                PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                postDTO.setSubredditName(post.getSubreddit().getName());
                postDTO.setUserName(post.getUser().getUsername());
                return postDTO;
            }).collect(Collectors.toList());
        }
        return MessageConstant.USER_NOT_FOUND;
    }


}
