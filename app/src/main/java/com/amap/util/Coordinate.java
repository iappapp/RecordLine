package com.amap.util;


/**
 * Created by app on 16/10/22.
 */
public class Coordinate {

    public static int[] toDegree(double longgitude){
        int longhour,longminute,longsecond;
        longhour = (int) longgitude;
        double latminuteTemp = (longgitude - longhour) * 60d;
        longminute = (int) latminuteTemp;
        longsecond = (int) ((latminuteTemp - longminute) * 60d);
        int[] longitudeInfo = new int[]{
            longhour,longminute,longsecond
        };
        return longitudeInfo;
    }

    //test
    public static void main(String[] args){
        for(int i : toDegree(112.23232))
        {
            System.out.print(i + " ");
        }
    }

}
