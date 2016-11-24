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
import android.widget.TextView;
import android.widget.Toast;

public class InputMeetingRoomVerifyActivity extends Activity {
	EditText et_inputMRI_MRGroupID;
	EditText et_inputMRI_pw;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			if(response.equals("验证成功!")){
				Toast.makeText(InputMeetingRoomVerifyActivity.this, "验证成功!", 0).show();
				Intent i1 = getIntent();
				if(i1.getIntExtra("flag", 0)==0){
					Intent i2= new Intent(InputMeetingRoomVerifyActivity.this,InputMeetingRoomInfo.class);
					i2.putExtra("mRGID",et_inputMRI_MRGroupID.getText().toString());
					startActivity(i2);
				}else{
					Intent i2= new Intent(InputMeetingRoomVerifyActivity.this,ShowMeetingRoomActivity.class);
					i2.putExtra("mRGID",et_inputMRI_MRGroupID.getText().toString());
					startActivity(i2);
				}
			}else{
				Toast.makeText(InputMeetingRoomVerifyActivity.this, "ID或密码错误!", 0).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_input_verify);
		
		et_inputMRI_MRGroupID = (EditText) findViewById(R.id.et_inputMRI_MRGroupID);
		et_inputMRI_pw = (EditText) findViewById(R.id.et_inputMRI_pw);
		Button b_inputMRI_verify = (Button) findViewById(R.id.b_inputMRI_verify);
		TextView tv_inputMRI_create = (TextView) findViewById(R.id.tv_inputMRI_create);
		
		tv_inputMRI_create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(InputMeetingRoomVerifyActivity.this,CreateMeetingRoomGroupIDActivity.class);
				startActivity(i);
				
			}
		});
		
		b_inputMRI_verify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!et_inputMRI_MRGroupID.getText().toString().equals("")){
					if(!et_inputMRI_pw.getText().toString().equals("")){
						InputMeetingRoomVerify();
					}else{
						Toast.makeText(InputMeetingRoomVerifyActivity.this, "密码不能为空!", 0).show();
					}
				}else{
					Toast.makeText(InputMeetingRoomVerifyActivity.this, "ID不能为空!", 0).show();
				}
				
			}
		});
	}
	private void InputMeetingRoomVerify(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/InputMeetingRoomVerify");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("mRGID", et_inputMRI_MRGroupID.getText().toString()));
					params.add(new BasicNameValuePair("pw", et_inputMRI_pw.getText().toString()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					System.out.println("111");
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						System.out.println("222");
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
