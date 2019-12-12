package com.select.school.utils.dxm.wechat;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpServletRequest;

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

    /**
     * http POST 请求
     */
    public static String doPost(String url, String params) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 设置配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                // 连接主机服务超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // 数据读取超时时间
                .setSocketTimeout(60000)
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json");
        // httpPost.addHeader("Authorization", BASIC);

        try {
            StringEntity strEntity = new StringEntity(params, "UTF-8");
            httpPost.setEntity(strEntity);
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, httpResponse);
        }
        return result;
    }

    /**
     * 关闭连接
     */
    private static void close(CloseableHttpClient httpClient,
                              CloseableHttpResponse httpResponse) {
        // 关闭资源
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String doPost123(String url, String param) throws Exception {
        StringBuilder responseStrBud = new StringBuilder();
        trustAllHosts();
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        boolean isHttps = url.toLowerCase().startsWith("https");
        if (isHttps) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) realUrl.openConnection();
//            httpsConn.setHostnameVerifier(DO_NOT_VERIFY);
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

    //获取IP
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
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


//    private static final HostnameVerifier DO_NOT_VERIFY = (HostnameVerifier) new Object();

    /**
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param outputStr     参数
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // 创建SSLContext
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
