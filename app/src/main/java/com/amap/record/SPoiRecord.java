package com.amap.record;

import java.util.Date;

/**
 * Created by tree on 17/5/7.
 */
public class SPoiRecord {
    private int id;
    private String name;
    private String description;
    private String point;
    private String address;
    private String time;
    private int userId;
    private int mobileId;

    public SPoiRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMobileId() {
        return mobileId;
    }

    public void setMobileId(int mobileId) {
        this.mobileId = mobileId;
    }

    @Override
    public String toString() {
        return "SPoiRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", point='" + point + '\'' +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", userId=" + userId +
                ", mobileId=" + mobileId +
                '}';
    }
}
