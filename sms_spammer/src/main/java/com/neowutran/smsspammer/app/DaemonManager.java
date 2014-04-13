package com.neowutran.smsspammer.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.neowutran.smsspammer.app.config.Config;
import com.neowutran.smsspammer.app.server.Status;


public class DaemonManager extends Activity {

    private static Boolean isRunning = false;
    private static String statusWaiting = null;
    private static Daemon.DaemonBinder daemonBinder;
    private static ServiceConnection daemonConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            daemonBinder = (Daemon.DaemonBinder) binder;
        }

        public void onServiceDisconnected(ComponentName className) {
            daemonBinder = null;
        }
    };
    private static DaemonManager instance;

    public static Boolean getIsRunning() {
        return isRunning;
    }

    public static void setStatusWaiting(final String statusWaiting) {
        DaemonManager.statusWaiting = statusWaiting;
    }

    public static DaemonManager getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
        instance = this;
        bindService(new Intent(this, Daemon.class), daemonConnection, BIND_AUTO_CREATE);
        setContentView(R.layout.activity_daemon_manager);
        Config.setProperties(this);
        updateStatus();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Daemon.setRunning(isChecked);
            }
        });

        Button button = (Button) findViewById(R.id.updateNow);
        button.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(final View view) {
                daemonBinder.checkSms();
            }
        });

        EditText updateInterval = (EditText) findViewById(R.id.updateInterval);
        updateInterval.setText(Config.getMinuteBetweenCheck());
        updateInterval.addTextChangedListener(new TextWatcher() {

            private String text = ((EditText) findViewById(R.id.updateInterval)).getText().toString();

            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (!editable.toString().equals(text)) {

                    Config.setMinuteBetweenCheck(editable.toString());
                    restartDaemon();
                    text = editable.toString();
                }
            }
        });

        final EditText apiUrl = (EditText) findViewById(R.id.input_api_url);
        apiUrl.setText(Config.getAPIUrl());
        apiUrl.addTextChangedListener(new TextWatcher() {

            private String text = apiUrl.getText().toString();

            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (!editable.toString().equals(text)) {
                    TextView status = (TextView) findViewById(R.id.connectionStatus);
                    status.setText((String) Config.getProperties().get(Status.PENDING));
                    Config.setAPIUrl(editable.toString());
                    text = editable.toString();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
        instance = this;
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(Daemon.getRunning());
        updateStatus();
    }

    private void updateStatus() {
        if (statusWaiting != null) {
            ((TextView) findViewById(R.id.connectionStatus)).setText(statusWaiting);
            statusWaiting = null;
        }
    }

    @Override
    public void onStop() {
        isRunning = false;
        instance = null;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        instance = null;
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
        return super.onOptionsItemSelected(item);
    }

    private void restartDaemon() {
        daemonBinder.killDaemon();
        startService(new Intent(this, Daemon.class));
    }


}
