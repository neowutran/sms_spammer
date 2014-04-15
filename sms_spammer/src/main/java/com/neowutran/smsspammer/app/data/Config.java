package com.neowutran.smsspammer.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.sms.Sms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    public static String getAPIUrl() {
        return preferences.getString("api_url", (String) properties.get("api_url"));
    }

    public static void setAPIUrl(String newApiUrl) {
        Logger.debug(Config.LOGGER, "new api url: " + newApiUrl);
        editor.putString("api_url", newApiUrl);
        editor.commit();
    }

    public static String getMinuteBetweenCheck() {
        return preferences.getString("minute_between_check", (String) properties.get("minute_between_check"));
    }


    public static void setMinuteBetweenCheck(String minuteBetweenCheck) {
        Logger.debug(Config.LOGGER, "new update check: " + minuteBetweenCheck);
        editor.putString("minute_between_check", minuteBetweenCheck);
        editor.commit();
    }

    public static List<Sms> getSms() {
        String sms = preferences.getString("sms", null);
        Logger.debug(Config.LOGGER, "get sms" + sms);
        Gson gson = new Gson();
        List<Sms> listSms = gson.fromJson(sms, new TypeToken<List<Sms>>() {
        }.getType());
        if(listSms == null){
            listSms = new ArrayList<>();
        }
        return listSms;
    }

    public static void setSms(List<Sms> sms) {
        Logger.debug(Config.LOGGER, "put sms");
        Gson gson = new Gson();
        editor.putString("sms", gson.toJson(sms, new TypeToken<List<Sms>>() {
        }.getType()));
        editor.commit();
    }

}
