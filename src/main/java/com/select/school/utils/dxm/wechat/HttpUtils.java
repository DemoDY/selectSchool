package com.select.school.utils.dxm.wechat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


public class HttpUtils {
    public static String sendGet(String url, String param) {
        trustAllHosts();
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = String.valueOf(url) + "?" + param;
            URL realUrl = new URL(urlNameString);

            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.connect();

            Map<String, List<String>> map = connection.getHeaderFields();

            for (String key : map.keySet()) {
                System.out.println(String.valueOf(key) + "--->" + map.get(key));
            }

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = String.valueOf(result) + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(param);

            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = String.valueOf(result) + line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������������" + e);
            e.printStackTrace();
        } finally {


            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String doPost(String url, String param) throws IOException {
        StringBuilder responseStrBud = new StringBuilder();
        trustAllHosts();
        URL realUrl = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

        boolean isHttps = url.toLowerCase().startsWith("https");
        if (isHttps) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) realUrl.openConnection();
            httpsConn.setHostnameVerifier(DO_NOT_VERIFY);
            connection = httpsConn;
        }

        connection.setRequestMethod("POST");

        connection.setConnectTimeout(15000);

        connection.setReadTimeout(60000);
        connection.setRequestProperty("Accept-Charset", "UTF-8");

        connection.setRequestProperty("Content-Type", " application/xml");


        connection.setDoOutput(true);

        connection.setDoInput(true);
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

            out.write(param);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                responseStrBud.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        connection.disconnect();
        return responseStrBud.toString();
    }


    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = {(TrustManager) new Object()};


        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final HostnameVerifier DO_NOT_VERIFY = (HostnameVerifier) new Object();
}
