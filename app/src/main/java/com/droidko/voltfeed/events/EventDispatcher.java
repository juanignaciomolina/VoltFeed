package com.droidko.voltfeed.events;

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

}
