package com.lzz.officeassistant.review;

import java.util.List;

import com.lzz.officeassistant.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<ReviewInfo> {
	private int resourceId;
	private Context context;
	public ReviewAdapter(Context context, int textViewresourceId, List<ReviewInfo> objects) {
		super(context, textViewresourceId, objects);
		resourceId = textViewresourceId;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReviewInfo ri = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView tv_itemReview_title = (TextView)view.findViewById(R.id.tv_itemReview_title);
		TextView tv_itemReview_requester = (TextView)view.findViewById(R.id.tv_itemReview_requester);
		TextView tv_itemReview_Rtime = (TextView)view.findViewById(R.id.tv_itemReview_Rtime);
		TextView tv_itemReview_Ctime = (TextView)view.findViewById(R.id.tv_itemReview_Ctime);
		TextView tv_itemReview_status = (TextView)view.findViewById(R.id.tv_itemReview_status);
		tv_itemReview_title.setText("标题:"+ri.getReviewTitle());
		tv_itemReview_requester.setText("请求人:"+ri.getRequester());
		if(ri.getStatus().equals("待审批")){
			tv_itemReview_Ctime.setText("请求时间:"+ri.getCommitTime());
		}else{
			tv_itemReview_Rtime.setText("回复时间:"+ri.getResponseTime());
		}
		tv_itemReview_status.setText(ri.getStatus());
		if(ri.getStatus().equals("待审批")){
			tv_itemReview_status.setTextColor(Color.parseColor("#979797"));
		}else if(ri.getStatus().equals("已通过")){
			tv_itemReview_status.setTextColor(Color.parseColor("#1CBE83"));
		}else{
			tv_itemReview_status.setTextColor(Color.parseColor("#D73232"));
		}
		
		return view;
	}
}
