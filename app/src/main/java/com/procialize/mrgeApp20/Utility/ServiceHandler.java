package com.procialize.mrgeApp20.Utility;


import android.annotation.SuppressLint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.apache.http.HttpVersion.HTTP_1_1;

//import org.apache.http.client.ClientProtocolException;

public class ServiceHandler {

    public final static int GET = 1;
    public final static int POST = 2;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    static String response = null;

    public ServiceHandler() {

    }

    private static HttpResponse transformResponse(Response response) {
        BasicHttpResponse httpResponse = null;
        try {
            int code = 0;
            if (response != null)
                code = response.code();


            try {

                if (response != null) {
                    String message = response.message();
                    httpResponse = new BasicHttpResponse(HTTP_1_1, code, message);

                    ResponseBody body = response.body();
                    InputStreamEntity entity = new InputStreamEntity(body.byteStream(), body.contentLength());
                    httpResponse.setEntity(entity);

                    Headers headers = response.headers();
                    for (int i = 0, size = headers.size(); i < size; i++) {
                        String name = headers.name(i);
                        String value = headers.value(i);
                        httpResponse.addHeader(name, value);
                        if ("Content-Type".equalsIgnoreCase(name)) {
                            entity.setContentType(value);
                        } else if ("Content-Encoding".equalsIgnoreCase(name)) {
                            entity.setContentEncoding(value);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;


            }
        });

        return builder.build();

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    @SuppressLint("Deprecation")
    public String makeServiceCall(String baseURL, int method, List<NameValuePair> params) {
        try {

            OkHttpClient client = null;
            try {

                URL url = new URL(baseURL);
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);

//				client = new OkHttpClient.Builder()
//						.connectTimeout(10, TimeUnit.SECONDS)
//						.writeTimeout(10, TimeUnit.SECONDS)
//						.readTimeout(30, TimeUnit.SECONDS)
//						.sslSocketFactory(NoSSLv3Factory)
//
//						.hostnameVerifier(new HostnameVerifier() {
//							@Override
//							public boolean verify(String hostname, SSLSession session) {
//								return true;
//							}
//						})
//					.build();

                client = getUnsafeOkHttpClient().newBuilder().build();


            } catch (Exception e) {
                e.printStackTrace();

            }


            HttpEntity httpEntity = null;
            Response httpResponse = null;


            Request request = null;
            if (params.size() > 0)
                request = new Request.Builder().url(baseURL).post(geBuilder(params).build()).build();
            else {
                request = new Request.Builder().url(baseURL).build();

            }

            try {
                httpResponse = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }


            httpEntity = transformResponse(httpResponse).getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } /*catch (ClientProtocolException e) {
            e.printStackTrace();
        }*/ catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return response;

    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private MultipartBody.Builder geBuilder(List<NameValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        MultipartBody.Builder buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (NameValuePair pair : params) {

            buildernew.addFormDataPart(pair.getName(), pair.getValue());

        }

        return buildernew;
    }
}