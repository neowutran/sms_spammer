package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * Created by neowutran on 10/04/14.
 */
public class SentListener extends BroadcastReceiver {

    private static SentListener sentListener = null;

    public static SentListener getInstance() {

        if (sentListener == null) {
            sentListener = new SentListener();
        }
        return sentListener;
    }

    private SentListener(){}


    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                System.out.println("SMS send successfully");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                System.out.println("SMS Sending Error :Generic failure");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                System.out.println("SMS Sending Error :No service");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                System.out.println("SMS Sending Error :Null PDU");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                System.out.println("SMS Sending Error :Radio off");
                break;
        }
    }
}
