package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.data.Config;

public class SentListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Logger.debug(Config.getProperties().getProperty("logger"), "SMS send successfully");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS Sending Error :Generic failure");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS Sending Error :No service");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS Sending Error :Null PDU");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS Sending Error :Radio off");
                break;
        }
    }
}
