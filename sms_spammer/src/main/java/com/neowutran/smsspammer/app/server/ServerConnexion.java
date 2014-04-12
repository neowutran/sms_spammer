package com.neowutran.smsspammer.app.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neowutran.smsspammer.app.Logger;
import com.neowutran.smsspammer.app.config.Config;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by draragar on 4/12/14.
 */
public class ServerConnexion extends Thread{

    public List<Map> getListSms() {
        return listSms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        ServerConnexion.status = status;
    }

    private static String status;

    public void acceptAllTheFuckingSSLCertificate() throws KeyManagementException, NoSuchAlgorithmException {
        //!!! This is a motherfucking dirty shitty hack to accept ssl certificate self signed.
        //!!! It will be easy for an attacker to exploit this shit.
        //!!! But I don't know good thing to accept self signed certificate.
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                }
        }, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
    }

    @Override
    public void run(){
        try {
            acceptAllTheFuckingSSLCertificate();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Logger.debug(Config.LOGGER, e.getMessage());
        }
        getSms();
    }

    private List<Map> listSms = new ArrayList<>();

    public void getSms(){
        this.listSms = new ArrayList<>();
        setStatus(Config.getProperties().getProperty("api_ok"));
        String json = "";

        try {
            URL url = new URL(Config.getAPIUrl());
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNextLine()) {
                json += scanner.nextLine();
            }

        } catch (IOException e) {
            Logger.error(Config.LOGGER, "wrong url:" + e.getMessage());
            status = Config.getProperties().getProperty("cannot_connect");
            return;
        }

        Gson gson = new Gson();
        Logger.debug(Config.LOGGER, "data = " + json);

        try {
            this.listSms = gson.fromJson(json, new TypeToken<List<Map>>() {
            }.getType());
        }catch(RuntimeException e){
            status = Config.getProperties().getProperty("wrong_data");
        }
    }


}
