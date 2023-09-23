package com.reddit.constant;

public class EmailConstants {

    private EmailConstants() {
        throw new IllegalArgumentException("EmailConstants is a utility class");
    }

    // Subject
    public static final String SUBJECT_ACCOUNT_VERIFY = "Account Verification";
    public static final String SUBJECT_COMMENT_POST = "Comment on post";
    // Variables
    public static final String USERNAME = "username";
    public static final String POST_NAME = "postName";

    // token
    public static final String TOKEN = "token";

    // Template Name
    public static final String TEMPLATE_ACCOUNT_VERIFY = "accountVerify";
    public static final String TEMPLATE_COMMENT_POST = "commentPost";


}
