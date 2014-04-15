package com.neowutran.smsspammer.app.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.SmsSpammerException;
import com.neowutran.smsspammer.app.data.Config;

import java.util.List;

public class Sms {

    private static List<Sms> listSms = Config.getSms();
    private String recipient;
    private String message;
    private int id;
    private SmsSendStatus sendStatus = SmsSendStatus.PENDING;
    private SmsServerStatus serverStatus = SmsServerStatus.PENDING;
    private static final String HEADER = "@NO-REPLY:\n";

    public Sms(String recipient, String message, int id) {
        this.recipient = recipient;
        this.message = HEADER + message;
        this.id = id;
    }

    public static Sms findSms(int id) {

        for (Sms sms : listSms) {
            if ((sms.id) == id) {
                return sms;
            }
        }
        return null;
    }

    public static void addSms(Sms newSms) {
        for (Sms sms : listSms) {
            if (sms.id == newSms.id) {
                return;
            }
        }
        listSms.add(newSms);
        Config.setSms(listSms);
    }

    public static void updateSms(Sms newSms) throws SmsSpammerException {
        for (int i = 0; i < listSms.size(); i++) {
            if (listSms.get(i).id == newSms.id) {
                listSms.remove(i);
                listSms.add(newSms);
                Config.setSms(listSms);
                return;
            }
        }

        throw new SmsSpammerException("SMS doesn't exist");

    }

    public static void sendAll(Context context) {
        for (Sms sms : listSms) {
            if (!sms.sendStatus.equals(SmsSendStatus.SUCCESS)) {
                sms.send(context);
            }
        }

    }

    private void send(Context context) {

        Logger.error(Config.LOGGER, "id3=" + id);
        Intent notificationSendIntent = new Intent();
        notificationSendIntent.setAction("com.neowutran.smsspammer.app.sms.SEND");
        notificationSendIntent.putExtra("id", id);

        Intent notificationDeliveredIntent = new Intent();
        notificationDeliveredIntent.setAction("com.neowutran.smsspammer.app.sms.DELIVERED");
        notificationDeliveredIntent.putExtra("id", id);


        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                notificationSendIntent, 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                notificationDeliveredIntent, 0);

        SmsManager.getDefault().sendTextMessage(recipient, null, message, sentPI, deliveredPI);
    }

    public int getId() {
        return id;
    }

    public SmsSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(final SmsSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public SmsServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(final SmsServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

}
