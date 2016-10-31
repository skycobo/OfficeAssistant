package com.lzz.officeassistant.notices;

import java.util.List;

import com.lzz.officeassistant.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoticeAdapter extends ArrayAdapter<Notice> {
	private int resourceId;
	private Context context;
	public NoticeAdapter(Context context, int textViewresourceId, List<Notice> objects) {
		super(context, textViewresourceId, objects);
		resourceId = textViewresourceId;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Notice notice = getItem(position);
		View view;
		if(convertView==null){
			view= LayoutInflater.from(getContext()).inflate(resourceId, null);
		}else{
			view = convertView;
		}
		TextView tv_title = (TextView)view.findViewById(R.id.tv_notice_title);
		TextView tv_content = (TextView)view.findViewById(R.id.tv_notice_content);
		TextView tv_time = (TextView)view.findViewById(R.id.tv_notice_time);
		TextView tv_flag = (TextView)view.findViewById(R.id.tv_notice_flag);
		tv_title.setText(notice.getTitle());
		tv_content.setText(notice.getContent());
		tv_time.setText(notice.getTime());
		SharedPreferences sp = context.getSharedPreferences("noticeflag", Context.MODE_APPEND);
		String flag = sp.getString(String.valueOf(position+1), null);
		if(flag!=null){
			tv_flag.setText(flag);
			tv_flag.setTextColor(android.graphics.Color.BLUE);
		}
		return view;
	}
	

}
