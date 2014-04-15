package com.neowutran.smsspammer.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.neowutran.smsspammer.app.data.Config;
import com.neowutran.smsspammer.app.server.ServerConnection;
import com.neowutran.smsspammer.app.server.Status;
import com.neowutran.smsspammer.app.sms.Sms;

import java.util.Calendar;
import java.util.Map;

public class Daemon extends Service {
    private static Boolean running = false;
    private final IBinder binder = new DaemonBinder();

    public static Boolean getRunning() {
        return running;
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
        Daemon.running = true;
        addServerSMS();
        return Service.START_NOT_STICKY;
    }

    private void addServerSMS() {
        ServerConnection readSms = new ServerConnection();
        readSms.start();

        try {
            readSms.join();
        } catch (InterruptedException e) {
            Logger.error(Config.LOGGER, e.getMessage());
        }

        if (readSms.getListSms() != null) {
            for (Map mapSms : readSms.getListSms()) {
                String recipient;
                String message;
                int id;
                try {
                    recipient = (String) mapSms.get("recipient");
                    message = (String) mapSms.get("message");
                    id = ((Double) mapSms.get("id")).intValue();
                } catch (RuntimeException e) {
                    Logger.error(Config.LOGGER, e.getMessage());
                    readSms.setStatus(Config.getProperties().getProperty(Status.WRONG_DATA));
                    return;
                }

                Sms sms = new Sms(recipient, message, id);
                Sms.addSms(sms);

            }
        }


        Intent notificationIntent = new Intent();
        notificationIntent.setAction("com.neowutran.smsspammer.app.Daemon");
        notificationIntent.putExtra("status", readSms.getStatus());
        this.sendBroadcast(notificationIntent);

        Sms.sendAll(this);

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

        public void killDaemon() {
            Daemon.this.stopSelf();
        }

        public void checkSms() {
            Daemon.this.addServerSMS();
        }
    }


}
