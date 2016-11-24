package com.lzz.officeassistant.review;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ReviewDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_detail);
		
		TextView tv_reviewDetail_title = (TextView)findViewById(R.id.tv_reviewDetail_title);
		TextView tv_reviewDetail_type = (TextView)findViewById(R.id.tv_reviewDetail_type);
		TextView tv_reviewDetail_requester = (TextView)findViewById(R.id.tv_reviewDetail_requester);
		TextView tv_reviewDetail_content = (TextView)findViewById(R.id.tv_reviewDetail_content);
		TextView tv_reviewDetail_commitTime = (TextView)findViewById(R.id.tv_reviewDetail_commitTime);
		TextView tv_reviewDetail_status = (TextView)findViewById(R.id.tv_reviewDetail_status);
		TextView tv_reviewDetail_idea = (TextView)findViewById(R.id.tv_reviewDetail_idea);
		TextView tv_reviewDetail_handler = (TextView)findViewById(R.id.tv_reviewDetail_handler);
		TextView tv_reviewDetail_responseTime = (TextView)findViewById(R.id.tv_reviewDetail_responseTime);
		
		Intent i = getIntent();
		ReviewInfo ri = (ReviewInfo) i.getSerializableExtra("review");
		tv_reviewDetail_title.setText("标题:"+ri.getReviewTitle());
		tv_reviewDetail_type.setText("类型:"+ri.getReviewType());
		tv_reviewDetail_requester.setText("请求人:"+ri.getRequester());
		tv_reviewDetail_content.setText("正文:"+ri.getReviewContent());
		tv_reviewDetail_commitTime.setText("提交时间:"+ri.getCommitTime());
		tv_reviewDetail_status.setText("审批状态:"+ri.getStatus());
		tv_reviewDetail_idea.setText("审批意见:"+ri.getReviewIdea());
		tv_reviewDetail_handler.setText("处理人:"+ri.getHandler());
		tv_reviewDetail_responseTime.setText("回复时间:"+ri.getResponseTime());
	}

}
