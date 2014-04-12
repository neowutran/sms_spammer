package com.neowutran.smsspammer.app.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String LOGGER = "neowutran.smsspammer";
    private static final String ERR_LOAD_CONFIG = "Unable to load the config file";
    private static final String CONFIG = "config.properties";
    private static Properties properties = null;

    private Config() {


    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Context context) {

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(CONFIG);
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e(LOGGER, ERR_LOAD_CONFIG);
        }

    }
}
