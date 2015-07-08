package com.droidko.voltfeed;

public class Schema {
    //** Start of PARSE SCHEMA **

    //GENERAL
    public static final String COL_OBJ_ID = "objectId";
    public static final String COL_CREATED_AT = "createdAt";
    public static final String COL_UPDATED_AT = "updatedAt";

    //USERS
    public static final String USERS_TABLE_NAME = "_Users";
    public static final String USERS_COL_USERNAME = "username";
    public static final String USERS_COL_PASSWORD = "password";
    public static final String USERS_COL_AUTH_DATA = "authData";
    public static final String USERS_COL_EMAIL_VERIFIED = "emailVerified";
    public static final String USERS_COL_COOVER = "cover";
    public static final String USERS_COL_DESCRIPTION = "description";
    public static final String USERS_COL_EMAIL = "email";
    public static final String USERS_COL_LOCATION = "location";
    public static final String USERS_COL_NAME = "name";
    public static final String USERS_COL_PHONE = "phone";
    public static final String USERS_COL_PICTURE = "picture";

    //POSTS
    public static final String POST_TABLE_NAME = "Posts";
    public static final String POST_COL_ID = "id";
    public static final String POST_COL_TYPE = "type";
    public static final int POST_COL_TYPE_IDEA = 0;
    public static final int POST_COL_TYPE_IMAGE = 1;
    public static final String POST_COL_LIKES = "likes";
    public static final String POST_COL_PICTURE = "picture";
    public static final String POST_COL_TEXT = "text";
    public static final String POST_COL_TITLE = "title";
    public static final String POST_COL_USER_ID = "userId";


    //** End of PARSE SCHEMA **
}
