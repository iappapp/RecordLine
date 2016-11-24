package com.amap.record;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;

import java.util.Date;
import java.util.List;

/**
 * Created by tree on 16/11/24.
 */
public class PoiRecord implements Parcelable {

    public PoiRecord() {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private int id;
    private String name;
    private String description;
    private String point;
    private String address;
    private Date time;
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(point);
        dest.writeString(address);
        dest.writeString(time.toString());
    }

    protected PoiRecord(Parcel parcel){

        this.name = parcel.readString();
        this.description = parcel.readString();
        this.point = parcel.readString();
        this.address = parcel.readString();
        this.time = new Date(parcel.readString());
    }

    public static final Parcelable.Creator<PoiRecord> CREATOR = new Parcelable.Creator<PoiRecord>(){
        @Override
        public PoiRecord createFromParcel(Parcel source) {
            return new PoiRecord(source);
        }

        @Override
        public PoiRecord[] newArray(int size) {
            return new PoiRecord[size];
        }
    };

}
