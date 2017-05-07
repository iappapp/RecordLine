package com.amap.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.amap.adapter.RecordAdapter;
import com.amap.api.maps.model.LatLng;
import com.amap.database.DbAdapter;
import com.amap.record.PathRecord;
import com.amap.util.Coordinate;
import com.facebook.rebound.ui.Util;

public class RecordActivity extends Activity {

	private RecordAdapter mAdapter;
	private ListView recordlist;
	private DbAdapter DbHepler;
	private Cursor mCursor;
	private List<PathRecord> listdata = new ArrayList<PathRecord>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordlist);
		recordlist = (ListView)findViewById(R.id.recordlist);
		DbHepler=new DbAdapter(this);
		DbHepler.open();
		addRecorddata();
		mAdapter = new RecordAdapter(this,listdata);
		recordlist.setAdapter(mAdapter);
		recordlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(position);
				Intent intent = new Intent(RecordActivity.this, RecordShowActivity.class);
				intent.putExtra("recorditem", recorditem);
				startActivity(intent);
			}
		});
		recordlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.i("MY", position+"");
				boolean b = DbHepler.delete(listdata.get(position).getId());
				if (b) {
					listdata.remove(position);
					mAdapter.notifyDataSetChanged();
					recordlist.invalidate();
				}
				return false;
			}
		});
	}

	private void addRecorddata() {
	
		mCursor = DbHepler.getallrecord();
		while (mCursor.moveToNext()) {
			PathRecord record = new PathRecord();
//			record.setId(mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_ROWID)));
			record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
			record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
			record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
			String lines = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_LINE));
			record.setPathline(Coordinate.parseLocations(lines));
			record.setStartpoint(Coordinate.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_STRAT))));
			record.setEndpoint(Coordinate.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_END))));
			listdata.add(record);
		}
		Collections.reverse(listdata);
	}

	
	public void onBackClick(View view) {
		this.finish();
	}

}
