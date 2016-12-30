package com.amap.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tree on 16/12/29.
 */
public class ToastUtils {
    public static void showText(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void showText(Context context,String message,int length){
        Toast.makeText(context,message,length).show();
    }
}
