package com.amap.util;

/**
 * Created by app on 16/10/26.
 */
public class Scale {
    private static int[] ruler = new int[]{
      10,20,50,100,200,400,900,1000,3000,7000,10000,30000,100000,400000,700000,900000
    };
    private static String[] rulers = new String[]{
        "10m","20m","50m","100m","200m","400m","900m","1Km","3Km","7Km","10Km","30Km","100Km","400Km","700Km","900Km"
    };

    public static String toAdjustUnit(float distance){
        if(distance < 100){
            return distance + "m";
        }else if(distance < 1000){
            return distance + "m";
        }else if(distance < 5000){
            return distance / 1000 + "km";
        }else if(distance < 10000){
            return distance / 5000 >= 1 ? "5Km" : "Km";
        }else
            return (distance = distance / 1000) + "Km";
    }

    public static String toAdjustUnits(float scale){
        for(int i = 0; i < ruler.length; i++){
            float imglength = ruler[i] / scale;
            int width = (int) imglength;
            if(imglength >= 40 && imglength <= 192){
                return rulers[i] + "," + width;
            }
        }
        return null;
    }
}
