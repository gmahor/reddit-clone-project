package com.reddit.constant;

public class GenericConstants {

    private GenericConstants() {
        throw new IllegalArgumentException("GenericConstants is a utility class");
    }

    public static final String TOKEN_MESSAGE ="token_message";


    // JWT
    public static final String ACCESS = "ACCESS";
    public static final String REFRESH = "REFRESH";

    // Swagger
    public static final String AUTHORIZATION = "Authorization";
    public static final String HEADER = "header";
    public static final String GLOBAL = "global";
    public static final String ACCESS_EVERYTHING = "accessEverything";

}