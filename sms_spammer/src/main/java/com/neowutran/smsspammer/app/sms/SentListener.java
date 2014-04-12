package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import com.neowutran.smsspammer.app.config.Config;

public class SentListener extends BroadcastReceiver {

    private static SentListener sentListener = null;

    private SentListener() {
    }

    public static SentListener getInstance() {

        if (sentListener == null) {
            sentListener = new SentListener();
        }
        return sentListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Log.i(Config.getProperties().getProperty("logger"), "SMS send successfully");
                //DO NOTHING
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Log.e(Config.getProperties().getProperty("logger"), "SMS Sending Error :Generic failure");
                //TODO notify server not good

                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Log.e(Config.getProperties().getProperty("logger"), "SMS Sending Error :No service");
                //TODO notify server not good

                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Log.e(Config.getProperties().getProperty("logger"), "SMS Sending Error :Null PDU");
                //TODO notify server not good

                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Log.e(Config.getProperties().getProperty("logger"), "SMS Sending Error :Radio off");
                //TODO notify server not good

                break;
        }
    }
}
