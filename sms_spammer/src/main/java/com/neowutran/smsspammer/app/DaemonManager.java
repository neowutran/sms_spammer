package com.neowutran.smsspammer.app;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.neowutran.smsspammer.app.config.Config;


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
    protected void onStart(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(Daemon.getRunning());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, Daemon.class), daemonConnection, BIND_AUTO_CREATE);
        setContentView(R.layout.activity_daemon_manager);
        Config.setProperties(this);


        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Daemon.setRunning(isChecked);
            }
        });

        EditText apiUrl = (EditText)findViewById(R.id.editText);
        apiUrl.setText(Config.getAPIUrl());
        apiUrl.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                Config.setAPIUrl(editable.toString());
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

    public class UIReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Bundle extras = intent.getExtras();
            if(extras != null){
                String status = (String)extras.get("status");
                ((TextView)findViewById(R.id.connectionStatus)).setText(status);
            }
        }
    }

}
