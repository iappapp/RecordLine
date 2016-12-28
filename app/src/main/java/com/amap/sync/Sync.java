package com.amap.sync;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.amap.record.PoiRecord;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 确定数据同步格式
 * 使用Json
 * Created by app on 16/12/25.
 */
public class Sync {

    public static void insertPoi(final PoiRecord poi,final CloseableHttpClient client,final String url){
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept","application/json");
        post.setHeader("Content-Type","application/json");
        String poistring = JSON.toJSONString(poi);
        StringEntity entity = null;
        try {
            entity = new StringEntity(poistring, "utf-8");
        }catch (UnsupportedEncodingException ex){
            Log.e("编码不支持",ex.getMessage());
        }
        post.setEntity(entity);
        try {
            client.execute(post);
        }catch (IOException ex){
            Log.e("IO异常",ex.getMessage());
        }
    }

    public static void deletePoi(final Integer id,final CloseableHttpClient client,final String url){
        HttpPost post = new HttpPost(url + "?id=" + id.intValue());
        try {
            client.execute(post);
        }catch (IOException ex){
            Log.e("IO异常,删除失败",ex.getMessage());
        }

    }


    public static List<Integer> gainNeedSyncPoi(final CloseableHttpClient client,final String url){
        HttpPost post = new HttpPost(url);
        HttpResponse response;
        HttpEntity httpEntity;
        InputStream inputStream;
        InputStreamReader reader;
        char[] buffer = new char[1024];
        int length = 0;
        StringBuilder sb = new StringBuilder();
        try{
            response = client.execute(post);
            httpEntity = response.getEntity();
            inputStream = httpEntity.getContent();
            reader = new InputStreamReader(inputStream,"utf-8");
            while((length = reader.read(buffer,0,buffer.length)) != -1){
                sb.append(buffer,0,length);
            }
            System.out.println("respone = " + sb.toString());
            reader.close();
            inputStream.close();
        }catch (IOException ex){
            Log.e("IO异常",ex.getMessage());
        }
        String result = sb.toString();
        return new ArrayList<>();
    }


}
