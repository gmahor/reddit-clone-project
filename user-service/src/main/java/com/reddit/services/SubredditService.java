package com.reddit.services;

import com.reddit.constant.MessageConstant;
import com.reddit.dtos.CreateSubredditDTO;
import com.reddit.dtos.SubredditDTO;
import com.reddit.entities.Subreddit;
import com.reddit.repositories.SubredditRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public SubredditService(SubredditRepository subredditRepository,
                            ModelMapper modelMapper) {
        this.subredditRepository = subredditRepository;
        this.modelMapper = modelMapper;

    }

    public Object createSubreddit(CreateSubredditDTO createSubredditDTO) {
        if (!subredditRepository.existsByName(createSubredditDTO.getName())) {
            Subreddit subreddit = new Subreddit();
            subreddit.setName(createSubredditDTO.getName());
            subreddit.setDescription(createSubredditDTO.getDescription());
            Subreddit savedSubreddit = saveSubreddit(subreddit);
            return modelMapper.map(savedSubreddit, SubredditDTO.class);
        }
        return MessageConstant.SUB_REDDIT_NAME_ALREADY_PRESENT;

    }

    @Transactional
    public Subreddit saveSubreddit(Subreddit subreddit) {
        return subredditRepository.save(subreddit);
    }

    public List<SubredditDTO> getAllSubreddits() {
        return subredditRepository.findAll().stream().map(subreddit -> modelMapper.map(subreddit, SubredditDTO.class)).collect(Collectors.toList());
    }

    public Object getSubreddit(Long id) {
        Optional<Subreddit> optionalSubreddit = subredditRepository.findById(id);
        if (optionalSubreddit.isPresent()) {
            Subreddit subreddit = optionalSubreddit.get();
            SubredditDTO subredditDTO = modelMapper.map(subreddit, SubredditDTO.class);
            subredditDTO.setNumberOfPosts(subreddit.getPosts().size());
            return subredditDTO;
        }
        return MessageConstant.SUB_REDDIT_NOT_FOUND;

    }


}
