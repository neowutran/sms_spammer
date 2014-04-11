package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by neowutran on 10/04/14.
 */
public class DeliveryListener extends BroadcastReceiver {
    private static DeliveryListener sentListener = null;

    public static DeliveryListener getInstance() {

        if (sentListener == null) {
            sentListener = new DeliveryListener();
        }
        return sentListener;
    }

    private DeliveryListener(){}

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (getResultCode())
        {
            case Activity.RESULT_OK:
                System.out.println("SMS delivered");
                break;
            case Activity.RESULT_CANCELED:
                System.out.println("SMS not delivered");
                break;
        }
    }
}
