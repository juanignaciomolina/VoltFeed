package com.droidko.voltfeed.events;

import com.droidko.voltfeed.model.Post;
import com.droidko.voltfeed.model.Volt;
import com.droidko.voltfeed.events.innerEvents.OnNoConnection;
import com.droidko.voltfeed.events.innerEvents.OnPublishEvent;
import com.droidko.voltfeed.events.innerEvents.OnTimelineImageClickEvent;
import com.droidko.voltfeed.events.innerEvents.OnVoltEvent;
import com.droidko.voltfeed.events.innerEvents.OnVoltsPostsUpdate;

import java.util.HashSet;

import de.greenrobot.event.EventBus;

public class EventDispatcher {

    //Event name: onNoConnection
    //Broadcasters: UiHelper
    //Listeners: TimelineHelper
    public static void dispatchNoConnection() {
        EventBus.getDefault().post(new OnNoConnection());
    }
    //----------------------------------------------------------------------------------------------

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

    //Event name: onVolt
    //Broadcasters: TimelineHelper
    //Listeners: None
    public static void dispatchVolt(Volt volt) {
        EventBus.getDefault().post(new OnVoltEvent(volt));
    }
    //----------------------------------------------------------------------------------------------

    //Event name: onVoltsPostsUpdate
    //Broadcasters: ApiHelper
    //Listeners: TimelineFragment
    public static void dispatchVoltsPostsUpdate(HashSet<String> voltedPostsSet) {
        EventBus.getDefault().post(new OnVoltsPostsUpdate(voltedPostsSet));
    }
    //----------------------------------------------------------------------------------------------
}
