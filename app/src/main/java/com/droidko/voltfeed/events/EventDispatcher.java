package com.droidko.voltfeed.events;

import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.events.innerEvents.OnPublishEvent;
import com.droidko.voltfeed.events.innerEvents.OnTimelineImageClickEvent;

import de.greenrobot.event.EventBus;

public class EventDispatcher {

    //Event name: onTimelineImage
    //Broadcasters: TimelineHelper
    //Listeners: MainActivity
    public static void dispatchTimelineImageClick(String fullResUri, String lowResUri) {
        EventBus.getDefault().post(new OnTimelineImageClickEvent(fullResUri, lowResUri));
    }
    //----------------------------------------------------------------------------------------------

    //Event name: onPublish
    //Broadcasters: NewPostFragment
    //Listeners: TimelineFragment
    public static void dispatchPublish(Post post) {
        EventBus.getDefault().post(new OnPublishEvent(post));
    }
    //----------------------------------------------------------------------------------------------
}
