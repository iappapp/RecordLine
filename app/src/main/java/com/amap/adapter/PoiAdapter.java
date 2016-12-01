package com.amap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amap.record.PoiRecord;

import java.util.List;

/**
 * Created by tree on 16/12/1.
 */
public class PoiAdapter extends BaseAdapter {



    private List<PoiRecord> poiRecordList;
    private Context context;


    public PoiAdapter(List<PoiRecord> poiRecordList, Context context) {
        this.poiRecordList = poiRecordList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
