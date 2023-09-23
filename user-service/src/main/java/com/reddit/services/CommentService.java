package com.reddit.services;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CommentDTO;
import com.reddit.dtos.CreateCommentDTO;
import com.reddit.entities.Comment;
import com.reddit.entities.Post;
import com.reddit.entities.User;
import com.reddit.repositories.CommentRepository;
import com.reddit.repositories.PostRepository;
import com.reddit.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final EmailService emailService;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          EmailService emailService,
                          ModelMapper modelMapper,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Object createComment(CreateCommentDTO createCommentDTO, User user) {
        Optional<Post> optionalPost = postRepository.findById(createCommentDTO.getPostId());
        if (optionalPost.isPresent()) {
            if (user != null) {
                Comment comment = new Comment();
                comment.setText(createCommentDTO.getText());
                comment.setPost(optionalPost.get());
                comment.setUser(user);
                Comment savedComment = commentRepository.save(comment);
                emailService.sendCommentNotification(user.getEmail(), user.getUsername(), optionalPost.get().getPostName());
                CommentDTO commentDTO = modelMapper.map(savedComment, CommentDTO.class);
                commentDTO.setCreatedDate(savedComment.getCreatedAt());
                commentDTO.setUserName(user.getUsername());
                return commentDTO;
            }
            return MessageConstant.USER_NOT_FOUND;
        }
        return MessageConstant.POST_NOT_FOUND;
    }


    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.map(post -> commentRepository.findByPost(post).stream().map(
                comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setCreatedDate(comment.getCreatedAt());
                    commentDTO.setUserName(comment.getUser().getUsername());
                    return commentDTO;
                }
        ).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    public List<CommentDTO> getAllCommentsForUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(user -> commentRepository.findAllByUser(user).stream().map(
                comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setCreatedDate(comment.getCreatedAt());
                    commentDTO.setUserName(comment.getUser().getUsername());
                    return commentDTO;
                }
        ).collect(Collectors.toList())).orElse(Collections.emptyList());
    }


}
