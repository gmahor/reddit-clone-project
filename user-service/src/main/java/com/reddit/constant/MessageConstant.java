package com.reddit.constant;


public class MessageConstant {

    private MessageConstant() {
        throw new IllegalArgumentException("MessageConstant is a utility class");
    }

    public static final String REQUEST_ERROR = "Error in request:: {}";
    public static final String SIGN_UP_SUCCESS = "SignUp successfully.";
    public static final String ERROR_SIGN_UP = "Error while signup user.";
    public static final String GET_USER_SUCCESS = "User get successfully.";
    public static final String ERROR_GETTING_USER = "Error while getting user.";
    public static final String ACCESS_DENIED = "ACCESS_DENIED : You don't have access to use this api. your are using some other user access token.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String SIGN_IN_SUCCESS = "Login successfully.";
    public static final String ERROR_SIGN_IN = "Error while login.";
    public static final String IN_CORRECT_PASSWORD = "Password is incorrect.";
    public static final String ACCOUNT_VERIFY_SUCCESS = "Account verified successfully.";
    public static final String ACCOUNT_VERIFY_ERROR = "Error while verifying account.";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid verification token.";
    public static final String VERIFICATION_TOKEN_EXPIRED = "Verification token is expired.";
    public static final String ACCOUNT_NOT_VERIFY = "Account verification link is send to your mail please activate your account.";
    public static final String SUB_REDDIT_CREATED_SUCCESS = "Subreddit created successfully.";
    public static final String SUB_REDDIT_CREATED_ERROR = "Error while creating subreddit.";
    public static final String ALL_SUB_REDDIT_SUCCESS = "All subreddits fetched successfully.";
    public static final String ALL_SUB_REDDIT_ERROR = "Error while fetching subreddits.";
    public static final String SUB_REDDIT_NOT_FOUND = "Subreddits not found.";
    public static final String SUB_REDDIT_FETCHED_SUCCESS = "Subreddits fetched successfully.";
    public static final String POST_CREATED_SUCCESS = "Post created successfully.";
    public static final String POST_CREATED_ERROR = "Error while creating post.";
    public static final String SUB_REDDIT_NAME_ALREADY_PRESENT = "Subreddit already present with this name.";
    public static final String POST_NOT_FOUND = "Post not found.";
    public static final String POST_FETCHED_SUCCESS = "Post fetched successfully.";
    public static final String POST_FETCHED_ERROR = "Error while fetching post.";
    public static final String POSTS_FETCHED_SUCCESS = "All posts fetched successfully.";
    public static final String POSTS_FETCHED_ERROR = "Error while fetching posts.";
    public static final String SUB_REDDIT_POSTS_SUCCESS = "All posts fetched successfully by subredditId.";
    public static final String SUB_REDDIT_POSTS_ERROR = "Error while fetching posts by subredditId.";
    public static final String USERNAME_POSTS_FETCHED_SUCCESS = "All posts fetched successfully by username.";
    public static final String USERNAME_POSTS_ERROR = "Error while fetching posts by username.";
    public static final String COMMENT_CREATED_SUCCESS = "Comment created successfully.";
    public static final String COMMENT_CREATED_ERROR = "Error while creating comment.";
    public static final String ALL_COMMENT_FOR_POST_SUCCESS = "All comments for post fetched successfully.";
    public static final String ALL_COMMENT_FOR_POST_ERROR = "Error while fetching comments for post.";
    public static final String ALL_COMMENT_FOR_USER_SUCCESS = "All comments for user fetched successfully.";
    public static final String ALL_COMMENT_FOR_USER_ERROR = "Error while fetching comments for user.";
    public static final String COMMENTS_NOT_FOUND = "Comments not found.";
    public static final String VOTE_SUCCESS = "Vote successfully.";
    public static final String VOTE_ERROR = "Error while voting.";

}
