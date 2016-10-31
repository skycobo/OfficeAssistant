package com.lzz.officeassistant.signin;

import java.util.List;

import com.lzz.officeassistant.R;
import com.lzz.officeassistant.notices.Notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SignInAdapter extends ArrayAdapter<SignInInfo> {
	private int resourceId;
	private Context context;
	public SignInAdapter(Context context, int textViewresourceId, List<SignInInfo> objects) {
		super(context, textViewresourceId, objects);
		resourceId = textViewresourceId;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SignInInfo s = getItem(position);
		View view;
		if(convertView==null){
			view= LayoutInflater.from(getContext()).inflate(resourceId, null);
		}else{
			view = convertView;
		}
		TextView tv_nickname = (TextView)view.findViewById(R.id.tv_signin_nickname);
		TextView tv_position = (TextView)view.findViewById(R.id.tv_signin_position);
		TextView tv_time = (TextView)view.findViewById(R.id.tv_signin_time);
		tv_nickname.setText(s.getNickname());
		tv_position.setText(s.getPosition());
		tv_time.setText(s.getTime());
		return view;
	}
}
