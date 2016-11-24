package com.lzz.officeassistant;

import java.io.File;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText et_login_account;
	private EditText et_login_pw;
	private TextView tv_login_register;
	private Button b_login_login;
	private CheckBox cb_login_remberPw;
	private String flag;
	private SharedPreferences sp;
	/**
	 * 回显信息
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String)msg.obj;
			if(response.substring(0, 7).equals("账号或密码错误")){
				Toast.makeText(LoginActivity.this, response, 0).show();
			}else{
				Toast.makeText(LoginActivity.this, "登陆成功!", 0).show();
				sp = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("account", et_login_account.getText().toString());
				edit.putString("pw", et_login_pw.getText().toString());
				edit.commit();
				saveUserInfo(response,et_login_account.getText().toString());
					Intent i = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(i);
				}
				
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		//findViewById()...
		et_login_account = (EditText) findViewById(R.id.et_login_account);
		et_login_pw = (EditText) findViewById(R.id.et_login_pw);
		tv_login_register = (TextView) findViewById(R.id.tv_login_register);
		b_login_login = (Button)findViewById(R.id.b_login_login);
		
		//回显账号和密码
		sp = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
		et_login_account.setText(sp.getString("account", null));
		et_login_pw.setText(sp.getString("pw", null));
		
		/**
		 * 转向注册页面功能
		 */
		tv_login_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(i);
				
			}
		});
		/**
		 * 登陆功能
		 */
		b_login_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//账号和密码都不为空时才会登陆
				if(!et_login_account.getText().toString().equals("")){
					if(!et_login_pw.getText().toString().equals("")){
						httpLogin();
					}else{
						Toast.makeText(LoginActivity.this, "密码不能为空!", 0).show();
					}
				}else{
					Toast.makeText(LoginActivity.this, "账号不能为空!", 0).show();
				}
				
			}
		});
	}
	private void httpLogin(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/login");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account", et_login_account.getText().toString()));
					params.add(new BasicNameValuePair("pw", et_login_pw.getText().toString()));
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
	private void saveUserInfo(String jsonData,String account){
		JSONObject jo = null;
		Editor edit = null;
		try {
			jo = new JSONObject(jsonData);
			sp = getSharedPreferences(account, Context.MODE_PRIVATE);
			edit = sp.edit();
			edit.putString("account", jo.getString("account"));
			edit.putString("pw", jo.getString("pw"));
			edit.putString("nickname",jo.getString("nickname"));
			if(jo.getString("teamID")!=null){
				edit.putString("teamID", jo.getString("teamID"));
			}
			if(jo.getString("teamName")!=null){
				edit.putString("teamName", jo.getString("teamName"));
			}
			if(jo.getString("teamCreater")!=null){
				edit.putString("teamCreater", jo.getString("teamCreater"));
			}
			if(jo.getString("teamCreaterName")!=null){
				edit.putString("teamCreaterName", jo.getString("teamCreaterName"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			if(edit!=null){
				edit.apply();
			}
		}
	}

}
