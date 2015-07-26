package com.droidko.voltfeed.utils;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.entities.Volt;
import com.droidko.voltfeed.events.EventDispatcher;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashSet;
import java.util.List;

public class ApiHelper {

    //** Start of PUBLISH **
    public static Post publishIdea(String ideaText) {
        ParseObject parsePost = new ParseObject(Schema.POST_TABLE_NAME);
        parsePost.put(Schema.POST_COL_TYPE, Schema.POST_COL_TYPE_IDEA);
        parsePost.put(Schema.POST_COL_TEXT, ideaText);
        parsePost.put(Schema.POST_COL_USER_ID, ParseUser.getCurrentUser().getUsername());
        parsePost.saveEventually();
        Post post = new Post(parsePost);
        EventDispatcher.dispatchPublish(post);
        return post;
    }
    //** End of PUBLISH **

    //** Start of TIMELINE **
    public static void getTimelinePosts(int actualPage, FindCallback findCallback) {
        getTimelinePosts(actualPage, findCallback, true);
    }

    public static void getTimelinePosts(int actualPage, FindCallback findCallback, boolean useCache) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Schema.POST_TABLE_NAME);
        parseQuery.orderByDescending(Schema.COL_CREATED_AT);
        parseQuery.setSkip(actualPage * Config.PERFORMANCE_API_PAGE_SIZE);
        parseQuery.setLimit(Config.PERFORMANCE_API_PAGE_SIZE);
        parseQuery.setMaxCacheAge(Config.PERFORMANCE_CACHE_MAX_AGE);
        //parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        if (!useCache) parseQuery.clearCachedResult();
        parseQuery.findInBackground(findCallback);
    }
    //** End of TIMELINE **

    //** Start of VOLTS **
    private static HashSet<String> sVoltedPostsSet = new HashSet<String>();

    public static Volt voltPost(Post post, boolean isVolted) {
        ParseObject parsePost = ParseObject.createWithoutData(Schema.POST_TABLE_NAME, post.getId());
        Volt volt = new Volt(isVolted);
        if (volt.getState()) {
            post.setVolts(post.getVolts() + 1);
            getVoltedPosts().add(post.getId());
            parsePost.increment(Schema.POST_COL_VOLTS, +1);
            ParseUser.getCurrentUser().getRelation(Schema.USER_COL_VOLTS_POSTS).add(parsePost);
        }
        else {
            post.setVolts(post.getVolts() - 1);
            getVoltedPosts().remove(post.getId());
            parsePost.increment(Schema.POST_COL_VOLTS, -1);
            ParseUser.getCurrentUser().getRelation(Schema.USER_COL_VOLTS_POSTS).remove(parsePost);
        }
        parsePost.saveEventually();
        ParseUser.getCurrentUser().saveEventually();
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
