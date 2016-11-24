package com.lzz.officeassistant;

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

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText et_register_account;
	private EditText et_register_pw1;
	private EditText et_register_pw2;
	private EditText et_register_nickname;
	private Button b_register_register;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String)msg.obj;
			if(response.substring(0, 4).equals("注册成功")){
				Toast.makeText(RegisterActivity.this, response, 0).show();
				onBackPressed();
			}else{
				Toast.makeText(RegisterActivity.this, response, 0).show();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//findViewById()...
		et_register_account = (EditText) findViewById(R.id.et_register_account);
		et_register_pw1 = (EditText) findViewById(R.id.et_register_pw1);
		et_register_pw2 = (EditText) findViewById(R.id.et_register_pw2);
		et_register_nickname = (EditText) findViewById(R.id.et_register_nickname);
		b_register_register = (Button) findViewById(R.id.b_register_register);  
		b_register_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				check();
			}
		});
	}
	private void check() {
		String regex ="\\w+@\\w{2,6}(\\.\\w{2,3})+";//定义邮箱规则
		boolean falg = et_register_account.getText().toString().matches(regex);
		if(et_register_account.getText().toString().equals("")||et_register_pw1.getText().toString().equals("")||
				et_register_pw2.getText().toString().equals("")||et_register_nickname.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "请完善信息!", 0).show();
		}else{
			if(falg){
				if(et_register_pw1.getText().toString().equals(et_register_pw2.getText().toString())){
					httpRegister();
				}else{
					Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入!", 0).show();
				}
			}else{
				Toast.makeText(RegisterActivity.this, "请检查邮箱格式!", 0).show();
			}
		}
	}
	
		
	private void httpRegister() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/register");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account", et_register_account.getText().toString()));
					params.add(new BasicNameValuePair("pw", et_register_pw1.getText().toString()));
					params.add(new BasicNameValuePair("nickname", et_register_nickname.getText().toString()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						Message message = new Message();
						message.obj = response.toString();
						handler.sendMessage(message);
						Log.i("tag", response);
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
