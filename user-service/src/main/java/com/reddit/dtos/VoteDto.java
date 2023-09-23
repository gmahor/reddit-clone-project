package com.reddit.dtos;

import com.reddit.enums.VoteType;
import lombok.Data;

@Data
public class VoteDto {

    private VoteType voteType;

    private Long postId;

}
