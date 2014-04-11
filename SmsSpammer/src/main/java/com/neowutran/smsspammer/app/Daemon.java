package com.neowutran.smsspammer.app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import com.neowutran.smsspammer.app.sms.DeliveryListener;
import com.neowutran.smsspammer.app.sms.SentListener;

/**
 * Created by neowutran on 11/04/14.
 */
public class Daemon extends Service {


    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("i m here");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        sendSMS();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(final Intent intent) {
         return null;
    }

    private void sendSMS()
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                                                          new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                                                               new Intent(DELIVERED), 0);
        //---when the SMS has been sent---
        registerReceiver(SentListener.getInstance(), new IntentFilter(SENT));
        //---when the SMS has been delivered---
        registerReceiver(DeliveryListener.getInstance(), new IntentFilter(DELIVERED));
        SmsManager.getDefault().sendTextMessage("0622666200", null, "phallus", sentPI, deliveredPI);
    }
}
