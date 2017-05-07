package com.amap.util;


import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

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

    public static ArrayList<LatLng> parseLocations(String latLonStr) {
        ArrayList<LatLng> latLonPoints = new ArrayList<LatLng>();
        String[] latLonStrs = latLonStr.split(";");
        for (int i = 0; i < latLonStrs.length; i++) {
            latLonPoints.add(parseLocation(latLonStrs[i]));
        }
        return latLonPoints;
    }

    public static LatLng parseLocation(String latLonStr) {
        if (latLonStr == null || latLonStr.equals("") || latLonStr.equals("[]")) {
            return null;
        }
        double lat = 0.0;
        double lon = 0.0;
        String[] loc = latLonStr.split(",");
        if (loc.length != 2) {
            return null;
        }
        lat = Double.parseDouble(loc[0]);
        lon = Double.parseDouble(loc[1]);
        return new LatLng(lat, lon);
    }

}
