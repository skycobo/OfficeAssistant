package com.lzz.officeassistant.notices;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.TextView;

public class NoticeContentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_content);
		TextView tv_noticeContent_title = (TextView) findViewById(R.id.tv_noticeContent_title);
		TextView tv_noticeContent_content = (TextView) findViewById(R.id.tv_noticeContent_content);
		TextView tv_noticeContent_time = (TextView) findViewById(R.id.tv_noticeContent_time);
		Intent i = getIntent();
		tv_noticeContent_title.setText(i.getStringExtra("title"));
		tv_noticeContent_content.setText(i.getStringExtra("content"));
		tv_noticeContent_time.setText(i.getStringExtra("time"));
		SharedPreferences sp = getSharedPreferences("noticeflag", Context.MODE_APPEND);
		Editor edit = sp.edit();
		edit.putString(String.valueOf(i.getIntExtra("id", 0)), "已读");
		edit.commit();
		
	}

}
