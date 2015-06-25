package com.droidko.voltfeed.events.innerEvents;

public class OnTimelineImageClickEvent {
    private String fullResUri;
    private String lowResUri;

    public OnTimelineImageClickEvent(String fullResUri, String lowResUri) {
        this.fullResUri = fullResUri;
        this.lowResUri = lowResUri;
    }

    public String getFullResUri() {
        return fullResUri;
    }

    public String getLowResUri() {
        return lowResUri;
    }
}
