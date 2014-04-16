package com.neowutran.smsspammer.app.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.data.Config;
import com.neowutran.smsspammer.app.server.nossl.HttpManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ServerConnection extends Thread {

    private static String status;
    private List<Map> listSms = new ArrayList<>();


    public List<Map> getListSms() {
        return listSms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        ServerConnection.status = status;
    }

    @Override
    public void run() {
        getSms();
    }

    public void getSms() {
        this.listSms = new ArrayList<>();
        setStatus(Config.getProperties().getProperty(Status.OK));
        String json = "";

        HttpGet request = new HttpGet();
        request.setHeader("Accept", "application/json");
        HttpClient httpClient = HttpManager.getNewHttpClient();
        try {
            try {
                request.setURI(new URI(Config.getAPIUrl()));
            } catch (RuntimeException e) {
                Logger.error(Config.LOGGER, "wrong url:" + e.getMessage());
                Config.resetApiUrl();
                request.setURI(new URI(Config.getAPIUrl()));
            }
            Logger.debug(Config.LOGGER, "url=" + Config.getAPIUrl());
            HttpResponse response = httpClient.execute(request);
            Scanner scanner = new Scanner(response.getEntity().getContent());
            while (scanner.hasNextLine()) {
                json += scanner.nextLine();
            }

        } catch (URISyntaxException | IOException e) {
            status = Config.getProperties().getProperty(Status.CANNOT_CONNECT);
            Logger.error(Config.LOGGER, "wrong url:" + e.getMessage());
            return;
        }

        Gson gson = new Gson();
        Logger.debug(Config.LOGGER, "data = " + json);

        try {
            this.listSms = gson.fromJson(json, new TypeToken<List<Map>>() {
            }.getType());
        } catch (RuntimeException e) {
            status = Config.getProperties().getProperty(Status.WRONG_DATA);
            Logger.error(Config.LOGGER, "wrong url:" + e.getMessage());

        }
    }

}
