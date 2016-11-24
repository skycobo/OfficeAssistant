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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ReviewEditActivity extends Activity {
	private EditText et_review_title;
	private EditText et_review_content;
	private Spinner sn_review_type;
	private BufferedWriter bw;
	private BufferedReader br;
	private SharedPreferences sp;
	private ReviewInfo reviewInfo = new ReviewInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_edit);
		
		et_review_title = (EditText) findViewById(R.id.et_review_title);
		et_review_content = (EditText) findViewById(R.id.et_review_content);
		sn_review_type = (Spinner) findViewById(R.id.sn_review_type);
		Button b_review_commit = (Button) findViewById(R.id.b_review_commit);
		
		sp = getSharedPreferences("currentUser",Context.MODE_PRIVATE);
		String currentUser = sp.getString("account", null);
		sp = getSharedPreferences(currentUser,Context.MODE_PRIVATE);
		reviewInfo.setRequester(sp.getString("nickname", null)+"("+sp.getString("account", null)+")");
		reviewInfo.setHandler(sp.getString("teamCreaterName", null)+"("+sp.getString("teamCreater", null)+")");
		reviewInfo.setReviewType(sn_review_type.getSelectedItem().toString());
		
		b_review_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_review_title.getText().toString().equals("")){
					if(!et_review_content.getText().toString().equals("")){
						AlertDialog.Builder adb = new AlertDialog.Builder(ReviewEditActivity.this);
						adb.setTitle("提交审批");
						adb.setMessage("确定提交?");
						adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int reviewCounter = sp.getInt("reviewCounter", 0);
								Editor edit = sp.edit();
								edit.putInt("reviewCounter", reviewCounter+1);
								edit.commit();
								reviewInfo.setReviewID(sp.getString("account", null)+"("+sp.getInt("reviewCounter", 0)+")");
								reviewInfo.setReviewType(sn_review_type.getSelectedItem().toString());
								reviewInfo.setReviewTitle(et_review_title.getText().toString());
								reviewInfo.setReviewContent(et_review_content.getText().toString());
								Date date = new Date();
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
								reviewInfo.setCommitTime(df.format(date));
								reviewInfo.setResponseTime("");
								reviewInfo.setReviewIdea("");
								reviewInfo.setStatus("待审批");
								JSONObject jo = new JSONObject();
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
									System.out.println(jsonStr);
									System.out.println(ja.toString());
									jo.put("reviewID",reviewInfo.getReviewID());
									jo.put("requester", reviewInfo.getRequester());
									jo.put("handler", reviewInfo.getHandler());
									jo.put("reviewType", reviewInfo.getReviewType());
									jo.put("reviewTitle", reviewInfo.getReviewTitle());
									jo.put("reviewContent",reviewInfo.getReviewContent());
									jo.put("commitTime",reviewInfo.getCommitTime());
									jo.put("status", reviewInfo.getStatus());
									jo.put("reviewIdea", reviewInfo.getReviewIdea());
									jo.put("responseTime", reviewInfo.getResponseTime());
									ja.put(jo);
									bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("review_"+sp.getString("account", null)+".json", MODE_PRIVATE)));
									bw.write(ja.toString());
									bw.flush();
									bw.close();
									reviewRequesterCommit();
									onBackPressed();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
						adb.show();
					
					}else{
						Toast.makeText(ReviewEditActivity.this, "内容不能为空!", 0).show();
					}
				}else{
					Toast.makeText(ReviewEditActivity.this, "标题不能为空!", 0).show();
				}
				
			}
		});
	}
	private void reviewRequesterCommit(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ReviewRequesterCommit");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("reviewID", reviewInfo.getReviewID()));
					params.add(new BasicNameValuePair("reviewTitle", reviewInfo.getReviewTitle()));
					params.add(new BasicNameValuePair("reviewType", reviewInfo.getReviewType()));
					params.add(new BasicNameValuePair("reviewContent", reviewInfo.getReviewContent()));
					params.add(new BasicNameValuePair("commitTime", reviewInfo.getCommitTime()));
					params.add(new BasicNameValuePair("requester", reviewInfo.getRequester()));
					params.add(new BasicNameValuePair("handler", reviewInfo.getHandler()));
					params.add(new BasicNameValuePair("status", reviewInfo.getStatus()));
					params.add(new BasicNameValuePair("reviewIdea", reviewInfo.getReviewIdea()));
					params.add(new BasicNameValuePair("responseTime", reviewInfo.getResponseTime()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					httpClient.execute(httpPost);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(httpClient!=null){
						httpClient.getConnectionManager().shutdown();
					}
				}
			}}
		).start();
	}

}
