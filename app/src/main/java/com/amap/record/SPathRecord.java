package com.amap.record;

/**
 * Created by tree on 17/5/7.
 */
public class SPathRecord {

    private Integer id;

    private String startpoint;

    private String distance;

    private String duration;

    private String averagespeed;

    private String date;

    private String endpoint;

    private Integer userId;

    private Integer mobileId;

    private String pathline;

    public SPathRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAveragespeed() {
        return averagespeed;
    }

    public void setAveragespeed(String averagespeed) {
        this.averagespeed = averagespeed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMobileId() {
        return mobileId;
    }

    public void setMobileId(Integer mobileId) {
        this.mobileId = mobileId;
    }

    public String getPathline() {
        return pathline;
    }

    public void setPathline(String pathline) {
        this.pathline = pathline;
    }
}

