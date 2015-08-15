package com.droidko.voltfeed.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.model.Post;
import com.droidko.voltfeed.model.Volt;
import com.droidko.voltfeed.events.EventDispatcher;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ApiHelper {

    //** Start of PUBLISH **
    private static Post publishGeneric(ParseObject parseObject) {
        ParseUser.getCurrentUser().increment(Schema.USER_COL_POSTS_CREATED_COUNT, +1);
        ParseUser.getCurrentUser().saveEventually();
        ParseUser.getCurrentUser().fetchInBackground();
        Post post = new Post(parseObject);
        EventDispatcher.dispatchPublish(post);
        return post;
    }

    public static Post publishIdea(String ideaText) {
        ParseObject parsePost = new ParseObject(Schema.POST_TABLE_NAME);
        parsePost.put(Schema.POST_COL_TYPE, Schema.POST_COL_TYPE_IDEA);
        parsePost.put(Schema.POST_COL_TEXT, ideaText);
        parsePost.put(Schema.POST_COL_USER_ID, ParseUser.getCurrentUser().getUsername());
        parsePost.saveEventually();

        return publishGeneric(parsePost);
    }

    public static Post publishImage(String imageText,String imageId) {
        ParseObject parsePost = new ParseObject(Schema.POST_TABLE_NAME);
        parsePost.put(Schema.POST_COL_TYPE, Schema.POST_COL_TYPE_IMAGE);
        parsePost.put(Schema.POST_COL_TEXT, imageText);
        parsePost.put(Schema.POST_COL_PICTURE, imageId);
        parsePost.put(Schema.POST_COL_USER_ID, ParseUser.getCurrentUser().getUsername());
        parsePost.saveEventually();

        return publishGeneric(parsePost);
    }
    //** End of PUBLISH **

    //** Start of TIMELINE **
    public static void getOlderTimelinePosts(@Nullable Date fromPostDate, FindCallback findCallback) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Schema.POST_TABLE_NAME);
        parseQuery.orderByDescending(Schema.COL_CREATED_AT);
        if (fromPostDate != null) parseQuery.whereLessThan(Schema.COL_CREATED_AT, fromPostDate);
        parseQuery.setLimit(Config.PERFORMANCE_API_PAGE_SIZE);
        //parseQuery.setMaxCacheAge(Config.PERFORMANCE_CACHE_MAX_AGE);
        //parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        parseQuery.findInBackground(findCallback);
    }

    public static void getNewerTimelinePosts(@NonNull Date latestPostDate, FindCallback findCallback) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Schema.POST_TABLE_NAME);
        parseQuery.orderByAscending(Schema.COL_CREATED_AT);
        parseQuery.whereGreaterThan(Schema.COL_CREATED_AT, latestPostDate);
        parseQuery.setLimit(Config.PERFORMANCE_API_PAGE_SIZE);
        //parseQuery.setMaxCacheAge(Config.PERFORMANCE_CACHE_MAX_AGE);
        //parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        parseQuery.findInBackground(findCallback);
    }
    //** End of TIMELINE **
    public static void getOlderConnectionsUsers(@Nullable Date fromPostDate, FindCallback findCallback) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.orderByDescending(Schema.COL_CREATED_AT);
        if (fromPostDate != null) parseQuery.whereLessThan(Schema.COL_CREATED_AT, fromPostDate);
        parseQuery.setLimit(Config.PERFORMANCE_API_PAGE_SIZE);
        //parseQuery.setMaxCacheAge(Config.PERFORMANCE_CACHE_MAX_AGE);
        //parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        parseQuery.findInBackground(findCallback);
    }

    public static void getNewerConnectionsUsers(@NonNull Date latestPostDate, FindCallback findCallback) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.orderByAscending(Schema.COL_CREATED_AT);
        parseQuery.whereGreaterThan(Schema.COL_CREATED_AT, latestPostDate);
        parseQuery.setLimit(Config.PERFORMANCE_API_PAGE_SIZE);
        //parseQuery.setMaxCacheAge(Config.PERFORMANCE_CACHE_MAX_AGE);
        //parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        parseQuery.findInBackground(findCallback);
    }
    //** Start of CONNECTIONS **

    //** End of CONNECTIONS *

    //** Start of VOLTS **
    private static HashSet<String> sVoltedPostsSet = new HashSet<String>();

    public static Volt voltPost(Post post, boolean isVolted) {
        ParseObject parsePost = ParseObject.createWithoutData(Schema.POST_TABLE_NAME, post.getId());
        Volt volt = new Volt(post.getId(), isVolted);
        if (volt.getState()) {
            post.setVolts(post.getVolts() + 1);
            getVoltedPosts().add(post.getId());
            parsePost.increment(Schema.POST_COL_VOLTS, +1);
            ParseUser.getCurrentUser().getRelation(Schema.USER_COL_VOLTS_POSTS).add(parsePost);
            ParseUser.getCurrentUser().increment(Schema.USER_COL_VOLTS_POSTS_COUNT, +1);
        } else {
            post.setVolts(post.getVolts() - 1);
            getVoltedPosts().remove(post.getId());
            parsePost.increment(Schema.POST_COL_VOLTS, -1);
            ParseUser.getCurrentUser().getRelation(Schema.USER_COL_VOLTS_POSTS).remove(parsePost);
            ParseUser.getCurrentUser().increment(Schema.USER_COL_VOLTS_POSTS_COUNT, -1);
        }
        parsePost.saveEventually();
        ParseUser.getCurrentUser().saveEventually();
        ParseUser.getCurrentUser().fetchInBackground();
        EventDispatcher.dispatchVolt(volt);
        EventDispatcher.dispatchVoltsPostsUpdate(getVoltedPosts());
        return volt;
    }

    public static void initVoltedPosts() {
        fetchVoltedPosts((new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e != null) {
                    UiHelper.showParseError(VoltfeedApp.getContextInstance(), e);
                } else {
                    populateSet(results);
                    EventDispatcher.dispatchVoltsPostsUpdate(getVoltedPosts());
                }
            }

            private void populateSet(List<ParseObject> results) {
                sVoltedPostsSet.clear();
                for (ParseObject parseObject : results) {
                    sVoltedPostsSet.add(parseObject.getObjectId());
                }
            }
        }));
    }

    public static HashSet<String> getVoltedPosts() {
        return sVoltedPostsSet;
    }

    public static boolean isPostVolted(Post post) {
        return getVoltedPosts().contains(post.getId());
    }

    //Any callback passed to this method will be called twice, first for the catched
    //results and later with the results from the network!
    protected static void fetchVoltedPosts(FindCallback<ParseObject> findCallback) {
        if (ParseUser.getCurrentUser() == null) return;
        ParseUser.getCurrentUser().fetchInBackground();
        ParseQuery<ParseObject> parseQuery =
                ParseUser
                        .getCurrentUser()
                        .getRelation(Schema.USER_COL_VOLTS_POSTS)
                        .getQuery();

        parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        parseQuery.findInBackground(findCallback);
    }
    //** End of VOLTS **
}
