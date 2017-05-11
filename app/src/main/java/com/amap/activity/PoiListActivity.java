package com.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.adapter.PoiAdapter;
import com.amap.api.maps.model.LatLng;
import com.amap.database.DbAdapter;
import com.amap.record.PathRecord;
import com.amap.record.PoiRecord;
import com.amap.util.Coordinate;
import com.amap.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tree on 16/12/1.
 */
public class PoiListActivity extends Activity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    private ListView poiList;
    private List<PoiRecord> poiRecords;
    private PoiAdapter poiAdapter;
    private DbAdapter db;
    private Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_list_activity);

        poiRecords = new ArrayList<>();
        initView();
        initEvent();
        initPoiData();
        initAdapter();
    }

    private void initView(){

        poiList = (ListView) findViewById(R.id.poi_listview);
    }

    public void initEvent(){
        poiList.setOnItemClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PoiRecord record = poiRecords.get(position);
        int index = record.getId();
        if(db.deletePoiRecord(index)){
            ToastUtils.showText(getApplicationContext(),"删除成功");
        }
        poiRecords.remove(position);
        poiAdapter.notifyDataSetChanged();
        poiList.invalidate();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiRecord record = poiRecords.get(position);
        List<LatLng> latLngList = new ArrayList<>(10);
        LatLng latLng = Coordinate.parseLocation(record.getPoint());
        latLngList.add(latLng);
        int index = record.getId();
        Intent intent = new Intent(getApplicationContext(), RecordShowActivity.class);
        PathRecord pathRecord = new PathRecord();
        pathRecord.setAveragespeed("0");
        pathRecord.setDate("");
        pathRecord.setPathline(latLngList);
        intent.putExtra("recorditem", pathRecord);
        startActivity(intent);
    }

    public void initAdapter(){
        poiAdapter = new PoiAdapter(poiRecords,PoiListActivity.this);
        poiList.setAdapter(poiAdapter);
    }


    private void initPoiData(){
        db = new DbAdapter(PoiListActivity.this);
        db.open();
        cursor = db.getAllPoiRecord();
        while(cursor.moveToNext()){
            PoiRecord record = new PoiRecord();
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setId(id);
            record.setName(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_NAME)));
            record.setDescription(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DESCRIPTION)));
            record.setPoint(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_POINT)));
            String date = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DATE));
            try{
                record.setTime(stringToDate(date));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            record.setName(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_NAME)));
            poiRecords.add(record);
        }
        //db.close();
    }

    private Date stringToDate(String date) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.parse(date);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
        finish();
    }
}
