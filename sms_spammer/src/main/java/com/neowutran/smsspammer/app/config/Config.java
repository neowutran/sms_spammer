package com.neowutran.smsspammer.app.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import com.neowutran.smsspammer.app.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String LOGGER = "neowutran.smsspammer";
    private static final String ERR_LOAD_CONFIG = "Unable to load the config file";
    private static final String CONFIG = "config.properties";
    private static Properties properties = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private Config() {


    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Context context) {
        preferences = context.getSharedPreferences("sms_spammer", Context.MODE_PRIVATE);
        editor = preferences.edit();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(CONFIG);
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            Logger.error(LOGGER, ERR_LOAD_CONFIG);
        }

    }

    public static void setAPIUrl(String newApiUrl){
        Logger.debug(Config.LOGGER, "new api url: "+newApiUrl);
        editor.putString("api_url", newApiUrl);
        editor.commit();
    }

    public static String getAPIUrl(){
        return preferences.getString("api_url", (String)properties.get("api_url"));
    }
}
