package com.droidko.voltfeed.utils;

import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.events.EventDispatcher;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PublishHelper {

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
}
