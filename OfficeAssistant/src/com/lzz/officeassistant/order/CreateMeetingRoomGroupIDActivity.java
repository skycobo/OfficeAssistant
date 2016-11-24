package com.lzz.officeassistant.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateMeetingRoomGroupIDActivity extends Activity {
	EditText et_CreateMRGroupID_MRGroupID;
	EditText et_CreateMRGroupID_pw;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			if(response.equals("创建成功!")){
				Toast.makeText(CreateMeetingRoomGroupIDActivity.this, "创建成功!", 0).show();
				Intent i = new Intent(CreateMeetingRoomGroupIDActivity.this,InputMeetingRoomVerifyActivity.class);
				startActivity(i);
			}else{
				Toast.makeText(CreateMeetingRoomGroupIDActivity.this, "该ID已被使用!", 0).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_mrgid_create);
		
		et_CreateMRGroupID_MRGroupID = (EditText) findViewById(R.id.et_CreateMRGroupID_MRGroupID);
		et_CreateMRGroupID_pw = (EditText)findViewById(R.id.et_CreateMRGroupID_pw);
		Button b_CreateMRGroupID_create = (Button) findViewById(R.id.b_CreateMRGroupID_create);
		
		b_CreateMRGroupID_create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!et_CreateMRGroupID_MRGroupID.getText().toString().equals("")){
					if(!et_CreateMRGroupID_pw.getText().toString().equals("")){
						CreateMeetingRoomGroupID();
					}else{
						Toast.makeText(CreateMeetingRoomGroupIDActivity.this, "密码不能为空!", 0).show();
					}
				}else{
					Toast.makeText(CreateMeetingRoomGroupIDActivity.this, "ID不能为空!", 0).show();
				}
				
			}
		});
		
	}
	private void CreateMeetingRoomGroupID(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/CreateMeetingRoomGroupID");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("mRGID", et_CreateMRGroupID_MRGroupID.getText().toString()));
					params.add(new BasicNameValuePair("pw", et_CreateMRGroupID_pw.getText().toString()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						Message message = new Message();
						message.obj = response.toString();
						handler.sendMessage(message);
					}
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
