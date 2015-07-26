package com.droidko.voltfeed.events.innerEvents;

import com.droidko.voltfeed.entities.Volt;

public class OnVoltEvent {

    private Volt mVolt;

    public OnVoltEvent(Volt volt) {
        mVolt = volt;
    }

    public Volt getVolt() {
        return mVolt;
    }

}
