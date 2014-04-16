package com.neowutran.smsspammer.app.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.SmsSpammerException;
import com.neowutran.smsspammer.app.data.Config;

public class DeliveryListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        Sms sms = Sms.findSms(id);

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Logger.debug(Config.getProperties().getProperty("logger"), "SMS delivered");
                sms.setSendStatus(SmsSendStatus.SUCCESS);
                //TODO notify server good
                break;
            case Activity.RESULT_CANCELED:
                Logger.error(Config.getProperties().getProperty("logger"), "SMS not delivered");
                sms.setSendStatus(SmsSendStatus.FAILURE);

                //TODO notify server not good
                break;
        }
        try {
            Sms.updateSms(sms);
        } catch (SmsSpammerException e) {
            Logger.error(Config.LOGGER, e.getMessage());
        }

    }

}
