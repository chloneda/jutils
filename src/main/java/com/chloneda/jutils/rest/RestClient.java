package com.chloneda.jutils.rest;

import com.chloneda.jutils.commons.StringFormater;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by chloneda
 * Description:
 */
public class RestClient {
    public static String METHOD_PARAM = "rc_method";
    public static String REQUEST_METHOD_GET = "GET";
    public static String REQUEST_METHOD_POST = "POST";
    public static String REQUEST_METHOD_PUT = "PUT";
    public static String REQUEST_METHOD_DELETE = "DELETE";
    private String contentTypeForXml = "application/xml";
    private String contentTypeForJson = "application/json";
    private String charset = "utf-8";

    public RestClient() {
    }

    public String request(String method, String uri, String data, Properties requestProperties) throws IOException {
        Validate.notEmpty(method, "method is empty!");
        Validate.notEmpty(uri, "uri is empty!");
        method = this.validateMethod(method);
        return this.innerRequest(method, uri, data, requestProperties);
    }

    protected String innerRequest(String method, String uri, String data, Properties requestProperties) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        String var8;
        try {
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("accept", this.contentTypeForXml);
            if (data != null && !data.isEmpty()) {
                connection.setRequestProperty("Content-Type",
                        StringFormater.format("{}; charset={}", new Object[]{this.contentTypeForXml, this.charset}));
            }

            this.fillRequestProperties(connection, requestProperties);
            connection.connect();
            if (data != null && !data.isEmpty()) {
                this.writeBody(connection, data);
            }

            var8 = this.readResponse(connection);
        } finally {
            connection.disconnect();
        }

        return var8;
    }

    protected String validateMethod(String method) {
        if (method.equalsIgnoreCase(REQUEST_METHOD_GET)) {
            return REQUEST_METHOD_GET;
        } else if (method.equalsIgnoreCase(REQUEST_METHOD_POST)) {
            return REQUEST_METHOD_POST;
        } else if (method.equalsIgnoreCase(REQUEST_METHOD_PUT)) {
            return REQUEST_METHOD_PUT;
        } else if (method.equalsIgnoreCase(REQUEST_METHOD_DELETE)) {
            return REQUEST_METHOD_DELETE;
        } else {
            throw new IllegalArgumentException(StringFormater.format("Unsupport request method '{}'!", new Object[]{method}));
        }
    }

    protected void fillRequestProperties(HttpURLConnection connection, Properties requestProperties) {
        if (requestProperties != null) {
            Iterator var4 = requestProperties.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry)var4.next();
                connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
            }

        }
    }

    protected String readResponse(HttpURLConnection connection) throws IOException {
        StringBuffer sbuf = new StringBuffer();
        BufferedReader reader = null;
        if (connection.getResponseCode() == 200) {
            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                    sbuf.append(line);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }

            }
        } else {



            sbuf.append(StringFormater.format("http status code : {}\n", new Object[]{connection.getResponseCode()}));
            sbuf.append(connection.getResponseMessage());
        }

        return sbuf.toString();
    }

    protected void writeBody(HttpURLConnection connection, String body) throws IOException {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.print(body);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }

        }

    }

    public String pesudoRequest(String method, String uri, String data, Properties requestProperties) throws IOException {
        Validate.notEmpty(method, "method is empty!");
        Validate.notEmpty(uri, "uri is empty!");
        method = this.validateMethod(method);
        if (method == REQUEST_METHOD_PUT) {
            method = REQUEST_METHOD_POST;
            uri = addMethodToUri(uri, "put");
        } else if (method == REQUEST_METHOD_DELETE) {
            method = REQUEST_METHOD_POST;
            uri = addMethodToUri(uri, "delete");
        }

        return this.innerRequest(method, uri, data, requestProperties);
    }

    public String get(String uri, String data, Properties requestProperties) throws IOException {
        return this.request(REQUEST_METHOD_GET, uri, data, requestProperties);
    }

    public String post(String uri, String data, Properties requestProperties) throws IOException {
        return this.request(REQUEST_METHOD_POST, uri, data, requestProperties);
    }

    public String put(String uri, String data, Properties requestProperties) throws IOException {
        return this.request(REQUEST_METHOD_PUT, uri, data, requestProperties);
    }

    public String pseudoPut(String uri, String data, Properties requestProperties) throws IOException {
        return this.pesudoRequest(REQUEST_METHOD_PUT, uri, data, requestProperties);
    }

    public String delete(String uri, String data, Properties requestProperties) throws IOException {
        return this.request(REQUEST_METHOD_DELETE, uri, data, requestProperties);
    }

    public String pseudoDelete(String uri, String data, Properties requestProperties) throws IOException {
        return this.pesudoRequest(REQUEST_METHOD_DELETE, uri, data, requestProperties);
    }

    public static String addMethodToUri(String uri, String method) {
        Validate.notEmpty(uri, "uri is empty!");
        Validate.notEmpty(method, "method is empty!");
        int index = uri.indexOf(METHOD_PARAM);
        if (index != -1) {
            return uri;
        } else {
            StringBuffer sbuf = new StringBuffer();
            sbuf.append(uri);
            index = uri.indexOf(63);
            if (index == -1) {
                sbuf.append('?');
            } else {
                sbuf.append('&');
            }

            sbuf.append(METHOD_PARAM);
            sbuf.append('=');
            sbuf.append(method);
            return sbuf.toString();
        }
    }

    public static String removeMethodFromUri(String uri) {
        Validate.notEmpty(uri, "uri is empty!");
        int index = uri.indexOf(METHOD_PARAM);
        return index == -1 ? uri : uri.substring(0, index - 1);
    }

    public String getContentType() {
        return this.contentTypeForXml;
    }

    public void setContentType(String contentType) {
        if (contentType != null && !contentType.isEmpty()) {
            this.contentTypeForXml = contentType;
        }
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
