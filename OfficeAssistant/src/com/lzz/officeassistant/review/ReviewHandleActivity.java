package com.lzz.officeassistant.review;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewHandleActivity extends Activity {
	private TextView tv_rh_title;
	private TextView tv_rh_type;
	private TextView tv_rh_requester;
	private TextView tv_rh_commitTime;
	private TextView tv_rh_content;
	private Button b_rh_agree;
	private Button b_rh_disagree;
	private EditText et_rh_idea;
	private ReviewInfo ri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_review_handle);
		
		tv_rh_title = (TextView) findViewById(R.id.tv_rh_title);
		tv_rh_type = (TextView)findViewById(R.id.tv_rh_type);
		tv_rh_requester = (TextView) findViewById(R.id.tv_rh_requester);
		tv_rh_commitTime = (TextView) findViewById(R.id.tv_rh_commitTime);
		tv_rh_content = (TextView) findViewById(R.id.tv_rh_content);
		et_rh_idea = (EditText) findViewById(R.id.et_rh_idea);
		b_rh_agree = (Button) findViewById(R.id.b_rh_agree);
		b_rh_disagree = (Button) findViewById(R.id.b_rh_disagree);
		
		Intent i = getIntent();
		ri = (ReviewInfo) i.getSerializableExtra("review");
		
		tv_rh_title.setText("标题:        "+ri.getReviewTitle());
		tv_rh_type.setText("类型:        "+ri.getReviewType());
		tv_rh_requester.setText("请求者:     "+ri.getRequester());
		tv_rh_commitTime.setText("请求时间: "+ri.getCommitTime());
		tv_rh_content.setText("正文:"+ri.getReviewContent());
	
		
		b_rh_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_rh_idea.getText().toString().equals("")){
					AlertDialog.Builder adb = new AlertDialog.Builder(ReviewHandleActivity.this);
					adb.setMessage("确定同意吗?");
					adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ri.setStatus("已通过"); 
							ri.setReviewIdea(et_rh_idea.getText().toString());
							Date date = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
							ri.setResponseTime(sdf.format(date));
							sendReviewResponse();
							saveHandleOverReview();
							onBackPressed();
						}
					});
					adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					adb.show();
				}else{
					Toast.makeText(ReviewHandleActivity.this, "请输入审批建议!", 0).show();
				}
			}
		});
		
		b_rh_disagree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_rh_idea.getText().toString().equals("")){
					AlertDialog.Builder adb = new AlertDialog.Builder(ReviewHandleActivity.this);
					adb.setMessage("确定否决吗?");
					adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ri.setStatus("已否决"); 
							ri.setReviewIdea(et_rh_idea.getText().toString());
							Date date = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
							ri.setResponseTime(sdf.format(date));
							sendReviewResponse();
							saveHandleOverReview();
							onBackPressed();
						}
					});
					adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					adb.show();
				}else{
					Toast.makeText(ReviewHandleActivity.this, "请输入审批建议!", 0).show();
				}
			}
		});
	}
	
	private void sendReviewResponse(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient hc = null;
				try{
					hc = new DefaultHttpClient();
					HttpPost hp = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/sendReviewResponse");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("reviewID", ri.getReviewID()));
					params.add(new BasicNameValuePair("reviewIdea", ri.getReviewIdea()));
					params.add(new BasicNameValuePair("responseTime", ri.getResponseTime()));
					params.add(new BasicNameValuePair("reviewStatus", ri.getStatus()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					hp.setEntity(entry);
					hc.execute(hp);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(hc!=null){
						hc.getConnectionManager().shutdown();
					}
				}
			}}).start();
	}
	
	private void saveHandleOverReview(){
		SharedPreferences sp = getSharedPreferences("currentUser",Context.MODE_PRIVATE);
		String currentUser = sp.getString("account", null);
		sp = getSharedPreferences(currentUser,Context.MODE_PRIVATE);
		
		int reviewCounter = sp.getInt("reviewCounter", 0);
		Editor edit = sp.edit();
		edit.putInt("reviewCounter", reviewCounter+1);
		edit.commit();
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			File file = new File("/data/data/com.lzz.officeassistant/files/review_"+sp.getString("account", null)+".json");
			if(!file.exists()){
				bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("review_"+sp.getString("account", null)+".json", MODE_PRIVATE)));
				JSONArray ja = new JSONArray();
				bw.write(ja.toString());
				bw.flush();
				bw.close();
			}
			br = new BufferedReader(new InputStreamReader(openFileInput("review_"+sp.getString("account", null)+".json")));
			String line =null;
			String jsonStr = "";
			while((line=br.readLine())!=null){
				jsonStr = jsonStr+line;
			}
			br.close();
			JSONArray ja = new JSONArray(jsonStr);
			JSONObject jo = new JSONObject();
			jo.put("reviewID",ri.getReviewID());
			jo.put("requester",ri.getRequester());
			jo.put("handler", ri.getHandler());
			jo.put("reviewType", ri.getReviewType());
			jo.put("reviewTitle", ri.getReviewTitle());
			jo.put("reviewContent",ri.getReviewContent());
			jo.put("commitTime",ri.getCommitTime());
			jo.put("status", ri.getStatus());
			jo.put("reviewIdea", ri.getReviewIdea());
			jo.put("responseTime",ri.getResponseTime());
			ja.put(jo);
			bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("review_"+sp.getString("account", null)+".json", MODE_PRIVATE)));
			bw.write(ja.toString());
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
