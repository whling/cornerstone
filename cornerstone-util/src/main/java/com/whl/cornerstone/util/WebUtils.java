package com.whl.cornerstone.util;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by whling on 2018/4/10.
 */
public class WebUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final String HTTPS = "https";
    private static final String TLS = "TLS";

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout)
            throws IOException {
        return doPost(url, params, "UTF-8", connectTimeout, readTimeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, params, charset, connectTimeout, readTimeout, null);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap)
            throws IOException {
        String ctype = new StringBuilder().append("application/x-www-form-urlencoded;charset=").append(charset).toString();
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if (query != null) {
            content = query.getBytes(charset);
        }
        return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap);
    }

    @Deprecated
    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout) throws IOException {
        return _doPost(url, ctype, content, connectTimeout, readTimeout, null);
    }

    private static String _doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap)
            throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = getConnection(new URL(url), "POST", ctype, headerMap);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, "UTF-8");
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;
        try {
            String ctype = new StringBuilder().append("application/x-www-form-urlencoded;charset=").append(charset).toString();
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), "GET", ctype, null);
            } catch (IOException e) {
                throw e;
            }
            try {
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap) throws IOException {
        HttpURLConnection conn = null;
        SSLContext ctx;
        if ("https".equals(url.getProtocol())) {
            ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e);
            }
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "top-sdk-java");
        conn.setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            for (Map.Entry entry : headerMap.entrySet()) {
                conn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return conn;
    }

    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }

        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?"))
                strUrl = new StringBuilder().append(strUrl).append(query).toString();
            else
                strUrl = new StringBuilder().append(strUrl).append("?").append(query).toString();
        } else if (strUrl.endsWith("&"))
            strUrl = new StringBuilder().append(strUrl).append(query).toString();
        else {
            strUrl = new StringBuilder().append(strUrl).append("&").append(query).toString();
        }

        return new URL(strUrl);
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if ((params == null) || (params.isEmpty())) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();

            if ((StringUtils.isNotEmpty(name)) && (StringUtils.isNotEmpty(value))) {
                if (hasParam)
                    query.append("&");
                else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        }
        String msg = getStreamAsString(es, charset);
        if (StringUtils.isEmpty(msg)) {
            throw new IOException(new StringBuilder().append(conn.getResponseCode()).append(":").append(conn.getResponseMessage()).toString());
        }
        throw new IOException(msg);
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();

            char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            String str = response.toString();
            return str;
        } finally {
            if (stream != null)
                stream.close();
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = "UTF-8";

        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if ((pair.length != 2) || (StringUtils.isEmpty(pair[1])))
                        break;
                    charset = pair[1].trim();
                    break;
                }

            }

        }

        return charset;
    }

    public static String decode(String value) {
        return decode(value, "UTF-8");
    }

    public static String encode(String value) {
        return encode(value, "UTF-8");
    }

    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Map<String, String> getParamsFromUrl(String url) {
        Map map = null;
        if ((url != null) && (url.indexOf(63) != -1)) {
            map = splitUrlQuery(url.substring(url.indexOf(63) + 1));
        }
        if (map == null) {
            map = new HashMap();
        }
        return map;
    }

    public static Map<String, String> splitUrlQuery(String query) {
        Map result = new HashMap();

        String[] pairs = query.split("&");
        if ((pairs != null) && (pairs.length > 0)) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if ((param != null) && (param.length == 2)) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }

    public static String doPost(String reqUrl, String xml) {
        DataInputStream input = null;
        ByteArrayOutputStream out = null;
        try {
            byte[] xmlData = xml.getBytes();
            URL url = new URL(reqUrl);
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);

            urlCon.setRequestProperty("Content-Type", "text/xml");
            urlCon.setRequestProperty("Content-length", String.valueOf(xmlData.length));
            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
            printout.write(xmlData);
            printout.flush();
            printout.close();
            input = new DataInputStream(urlCon.getInputStream());

            out = new ByteArrayOutputStream();
            byte[] bufferByte = new byte[256];
            int l = -1;
            int downloadSize = 0;
            while ((l = input.read(bufferByte)) > -1) {
                downloadSize += l;
                out.write(bufferByte, 0, l);
                out.flush();
            }
            byte[] rResult = out.toByteArray();
            String str = new String(rResult, "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                input.close();
            } catch (Exception localException3) {
            }
        }
        return null;
    }

    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }
}
