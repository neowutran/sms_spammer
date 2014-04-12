package com.neowutran.smsspammer.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class DaemonManager extends Activity {

    private static Daemon daemon;

    private static ServiceConnection daemonConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            Daemon.DaemonBinder bind = (Daemon.DaemonBinder) binder;
            daemon = bind.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            daemon = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, Daemon.class), daemonConnection, BIND_AUTO_CREATE);
        setContentView(R.layout.activity_daemon_manager);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(Daemon.getRunning());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Daemon.setRunning(isChecked);
            }
        });
    }

    @Override
    public void onDestroy() {
        unbindService(daemonConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daemon_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}
