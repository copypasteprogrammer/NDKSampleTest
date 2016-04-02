package com.example.andrius.ndksampletest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * client-side interface to the back-end application.
 */
public class Api {

    private SSLContext sslContext;
    private int lastResponseCode;

    public int getLastResponseCode() {
        return lastResponseCode;
    }

    public Api(AuthenticationParameters authParams) throws Exception {

        File clientCertFile = authParams.getClientCertificate();

        sslContext = SSLContextFactory.getInstance().makeContext(clientCertFile, authParams.getClientCertificatePassword(), authParams.getCaCertificate());

        CookieHandler.setDefault(new CookieManager());
    }

    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("https://192.168.1.71:8443/NaujasProjektas/Connector", session);
        }
    };



    public String doGet(String url)  throws Exception {
        String result = null;

        HttpURLConnection urlConnection = null;
        try {
            URL requestedUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestedUrl.openConnection();

            if(urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
                ((HttpsURLConnection) urlConnection).setHostnameVerifier(hostnameVerifier);
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1500);

            lastResponseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());

        } catch(Exception ex) {
            result = ex.toString();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}