package com.neowutran.smsspammer.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UIReceiver extends BroadcastReceiver {

    public UIReceiver() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String status = (String) extras.get("status");
            if (DaemonManager.getIsRunning()) {
                ((TextView) DaemonManager.getInstance().findViewById(R.id.connectionStatus)).setText(status);
            } else {
                DaemonManager.setStatusWaiting(status);
            }
        }
    }
}

