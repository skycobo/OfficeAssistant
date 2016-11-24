package com.lzz.officeassistant.order;

import java.util.List;

import com.lzz.officeassistant.R;
import com.lzz.officeassistant.review.ReviewInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MeetingRoomAdapter extends ArrayAdapter<MeetingRoom> {
	private int resourceId;
	private Context context;
	public MeetingRoomAdapter(Context context, int textViewresourceId, List<MeetingRoom> objects) {
		super(context, textViewresourceId, objects);
		resourceId = textViewresourceId;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MeetingRoom mr = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tv_itemRM_name = (TextView)view.findViewById(R.id.tv_itemRM_name);
		TextView tv_itemRM_accommodate = (TextView)view.findViewById(R.id.tv_itemRM_accommodate);
		TextView tv_itemRM_location = (TextView)view.findViewById(R.id.tv_itemRM_location);
		
		tv_itemRM_name.setText("会议室名称:"+mr.getName());
		tv_itemRM_accommodate.setText("容纳人数:"+mr.getAccommodate());
		tv_itemRM_location.setText("具体位置:"+mr.getLocation()+mr.getFloor());
		return view;
	}
}

