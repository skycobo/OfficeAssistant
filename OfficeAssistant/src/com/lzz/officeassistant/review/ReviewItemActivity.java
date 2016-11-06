package com.lzz.officeassistant.review;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_item);
		TextView tv_AReviewItem_content = (TextView) findViewById(R.id.tv_AReviewItem_content);
		Intent i = getIntent();
		BufferedReader br=null;
		JSONArray ja = null;
		String data [] =null;
		try {
			br = new BufferedReader(new InputStreamReader(this.openFileInput("review.json")));
			ja = new JSONArray(br.readLine());
			br.close();
			JSONObject jo = ja.getJSONObject(i.getIntExtra("id", 0));
			String info = "标题:"+jo.getString("reviewTitle")+"\n类型:"+jo.getString("reviewType")+"\n正文:\n"+jo.getString("reviewContent")
			        +"\n提交时间:"+jo.getString("requestTime")+"\n申请人:"+jo.getString("requester")+"\n处理人:"+jo.getString("handler")
			        +"\n审批状态:"+jo.getString("status")+"\n审批意见:"+jo.getString("reviewIdea")+"\n回复时间:"+jo.getString("responseTime");
			tv_AReviewItem_content.setText(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
