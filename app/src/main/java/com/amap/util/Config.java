package com.amap.util;

import android.os.Environment;

/**
 * Created by tree on 17/1/1.
 */
public class Config {

    public static final String HOST = "http://192.168.3.24";
    public static final String PORT = "8080";
    public static final String BASEURL = HOST + ":" + PORT;
    public static final String CONTEXT = "";
    public static final String DEFAULT_USER_ICON = "default.jpg";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordPath";
}
