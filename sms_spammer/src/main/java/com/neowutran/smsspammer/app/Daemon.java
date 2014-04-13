package com.neowutran.smsspammer.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import com.neowutran.smsspammer.app.config.Config;
import com.neowutran.smsspammer.app.server.ServerConnection;
import com.neowutran.smsspammer.app.server.Status;
import com.neowutran.smsspammer.app.sms.DeliveryListener;
import com.neowutran.smsspammer.app.sms.SentListener;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by neowutran on 11/04/14.
 */
public class Daemon extends Service {
    private static Boolean running = false;
    private final IBinder binder = new DaemonBinder();

    public static Boolean getRunning() {
        return running;
    }

    public static void setRunning(final Boolean running) {
        Daemon.running = running;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.running = true;
        Config.setProperties(this);

        Calendar cal = Calendar.getInstance();
        Intent intentService = new Intent(this, Daemon.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intentService, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60 * 1000 * Integer.valueOf(Config.getMinuteBetweenCheck()), pintent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (running) {
            checkSMS();
        }
        return Service.START_NOT_STICKY;
    }

    private void checkSMS() {
        ServerConnection readSms = new ServerConnection();

        readSms.start();

        try {
            readSms.join();
        } catch (InterruptedException e) {
            Logger.error(Config.LOGGER, e.getMessage());
        }

        if (readSms.getListSms() != null) {
            for (Map sms : readSms.getListSms()) {
                String recipient;
                String message;
                String id;
                try {
                    recipient = (String) sms.get("recipient");
                    message = (String) sms.get("message");
                    id = (String) sms.get("id");
                } catch (RuntimeException e) {
                    readSms.setStatus(Config.getProperties().getProperty(Status.WRONG_DATA));
                    return;
                }

                sendSMS(recipient, message, id);
            }
        }

        Intent notificationIntent = new Intent();
        notificationIntent.setAction("com.neowutran.smsspammer.app.Daemon");
        notificationIntent.putExtra("status", readSms.getStatus());
        this.sendBroadcast(notificationIntent);
    }

    private void sendSMS(String recipient, String message, String id) {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        Intent deliveredIntent = new Intent(DELIVERED);
        deliveredIntent.putExtra("id", id);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(SentListener.getInstance(), new IntentFilter(SENT));
        //---when the SMS has been delivered---
        registerReceiver(DeliveryListener.getInstance(), new IntentFilter(DELIVERED));
        SmsManager.getDefault().sendTextMessage(recipient, null, message, sentPI, deliveredPI);
    }

    @Override
    public void onDestroy() {
        Daemon.running = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return binder;
    }

    public class DaemonBinder extends Binder {
        public Daemon getService() {
            return Daemon.this;
        }

        public void killDaemon() {
            Daemon.this.stopSelf();
        }

        public void checkSms() {
            Daemon.this.checkSMS();
        }
    }


}
