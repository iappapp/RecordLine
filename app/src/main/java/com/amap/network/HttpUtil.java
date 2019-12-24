package com.amap.network;

import com.alibaba.fastjson.JSON;

import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();

    /**
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        return response.body().string();
    }


    public static <T> T get(String url, Class<T> T) throws Exception {
        return JSON.parseObject(HttpUtil.get(url), T);
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        for (String param : params.keySet()) {
            builder.append(param + "=" + params.get(param) + "&");
        }

        return HttpUtil.get(builder.toString());
    }

    public static <T> T get(String url, Map<String, String> params, Class<T> T) throws Exception {
        return JSON.parseObject(HttpUtil.get(url, params), T);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(HttpUtil.get("http://souhu.com", String.class));
    }


}
