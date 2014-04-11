package com.neowutran.smsspammer.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.neowutran.smsspammer.app.config.Config;

/**
 * Created by neowutran on 11/04/14.
 */
public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Config.setProperties(context);
        context.startService(new Intent(context, Daemon.class));
    }
}
