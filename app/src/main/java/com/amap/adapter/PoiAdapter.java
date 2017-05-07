package com.amap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.activity.R;
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
        return poiRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.poiitem,null);
            holder.poi = (ImageView) convertView.findViewById(R.id.poi_image);
            holder.content = (TextView) convertView.findViewById(R.id.poi_content);
            holder.position = (TextView) convertView.findViewById(R.id.poi_position);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        PoiRecord item = poiRecordList.get(position);
        holder.content.setText(item.getName());
        holder.position.setText(item.getPoint());

        return convertView;
    }

    private class ViewHolder{
        ImageView poi;
        TextView content;
        TextView position;
    }

}
