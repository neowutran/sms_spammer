package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.config.Config;

public class DeliveryListener extends BroadcastReceiver {
    private static DeliveryListener sentListener = null;

    private DeliveryListener() {
    }

    public static DeliveryListener getInstance() {

        if (sentListener == null) {
            sentListener = new DeliveryListener();
        }
        return sentListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Logger.debug(Config.getProperties().getProperty("logger"), "SMS delivered");
                //TODO notify server good
                break;
            case Activity.RESULT_CANCELED:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS not delivered");
                //TODO notify server not good
                break;
        }
    }
}
