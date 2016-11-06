package com.lzz.officeassistant.review;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditReviewActivity extends Activity {
	private EditText et_review_title;
	private EditText et_review_content;
	private Spinner sn_review_type;
	private BufferedWriter bw;
	private BufferedReader br;
	private ReviewInfo reviewInfo = new ReviewInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_edit);
		
		et_review_title = (EditText) findViewById(R.id.et_review_title);
		et_review_content = (EditText) findViewById(R.id.et_review_content);
		sn_review_type = (Spinner) findViewById(R.id.sn_review_type);
		Button b_review_commit = (Button) findViewById(R.id.b_review_commit);
		
		SharedPreferences sp = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		reviewInfo.setRequester(sp.getString("nickname", null)+"("+sp.getString("account", null)+")");
		reviewInfo.setHandler(sp.getString("teamCreaterName", null)+"("+sp.getString("teamCreater", null)+")");
		reviewInfo.setReviewType(sn_review_type.getSelectedItem().toString());
		
		b_review_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_review_title.getText().toString().equals("")){
					if(!et_review_content.getText().toString().equals("")){
						AlertDialog.Builder adb = new AlertDialog.Builder(EditReviewActivity.this);
						adb.setTitle("提交审批");
						adb.setMessage("确定提交?");
						adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								reviewInfo.setReviewType(sn_review_type.getSelectedItem().toString());
								reviewInfo.setReviewTitle(et_review_title.getText().toString());
								reviewInfo.setReviewContent(et_review_content.getText().toString());
								Date date = new Date();
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
								reviewInfo.setRequestTime(df.format(date));
								reviewInfo.setResponseTime("");
								reviewInfo.setReviewIdea("");
								reviewInfo.setStatus("审批中");
								JSONObject jo = new JSONObject();
								try {
									File file = new File("/data/data/com.lzz.officeassistant/files/review.json");
									System.out.println(file.toString());
									System.out.println("edit--"+file.exists());
									if(!file.exists()){
										bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("review.json", MODE_PRIVATE)));
										JSONArray ja = new JSONArray();
										bw.write(ja.toString());
										bw.flush();
										bw.close();
									}
									br = new BufferedReader(new InputStreamReader(openFileInput("review.json")));
									String line =null;
									String jsonStr = "";
									while((line=br.readLine())!=null){
										jsonStr = jsonStr+line;
									}
									br.close();
									JSONArray ja = new JSONArray(jsonStr);
									System.out.println(jsonStr);
									System.out.println(ja.toString());
									jo.put("requester", reviewInfo.getRequester());
									jo.put("handler", reviewInfo.getHandler());
									jo.put("reviewType", reviewInfo.getReviewType());
									jo.put("reviewTitle", reviewInfo.getReviewTitle());
									jo.put("reviewContent",reviewInfo.getReviewContent());
									jo.put("requestTime",reviewInfo.getRequestTime());
									jo.put("status", reviewInfo.getStatus());
									jo.put("reviewIdea", reviewInfo.getReviewIdea());
									jo.put("responseTime", reviewInfo.getResponseTime());
									ja.put(jo);
									bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("review.json", MODE_PRIVATE)));
									bw.write(ja.toString());
									bw.flush();
									bw.close();
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
						Toast.makeText(EditReviewActivity.this, "内容不能为空!", 0).show();
					}
				}else{
					Toast.makeText(EditReviewActivity.this, "标题不能为空!", 0).show();
				}
				
			}
		});
	}

}
