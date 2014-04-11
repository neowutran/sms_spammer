package com.neowutran.smsspammer.app.server;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by draragar on 4/11/14.
 */
public class Data_Stub {

    public static Map getSms(){
        String json = "{\"recipient\":\"0622666200\", \"message\":\"Coucou\", \"id\": \"1\"}";
      //  Gson gson = new Gson();
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);

    }


}
