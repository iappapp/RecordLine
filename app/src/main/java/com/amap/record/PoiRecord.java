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

    private int id;
    private String description;
    private List<LatLng> point;
    private Date time;

    public PoiRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LatLng> getPoint() {
        return point;
    }

    public void setPoint(List<LatLng> point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeTypedList(point);
        dest.writeString(time.toString());
    }

    protected PoiRecord(Parcel parcel){
        this.id = parcel.readInt();
        this.description = parcel.readString();
        this.point = parcel.createTypedArrayList(LatLng.CREATOR);
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
