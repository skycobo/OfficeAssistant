package com.lzz.officeassistant.me;

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
import android.content.Context;
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

public class CreateTeamActivity extends Activity {
	EditText et_createTeam_teamID;
	EditText et_createTeam_teamName;
	Button b_createTeam;
	String teamCreater;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String)msg.obj;
			if(response.substring(0, 5).equals("创建成功!")){
				Toast.makeText(CreateTeamActivity.this, response, 0).show();
				SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("teamID", et_createTeam_teamID.getText().toString());
				edit.putString("teamName",et_createTeam_teamName.getText().toString());
				edit.putString("teamCreater",teamCreater);
				edit.commit();
				onBackPressed();
			}else{
				Toast.makeText(CreateTeamActivity.this, response, 0).show();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);
		
		et_createTeam_teamID = (EditText) findViewById(R.id.et_createTeam_teamID);
		et_createTeam_teamName = (EditText) findViewById(R.id.et_createTeam_teamName);
		b_createTeam = (Button) findViewById(R.id.b_createTeam);
		
		b_createTeam.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				check();
			}

		});
	}
	/**
	 * 检查输入是否为空等
	 */
	private void check() {
		if(!et_createTeam_teamID.getText().toString().equals("")){
			if(!et_createTeam_teamName.getText().toString().equals("")){
				createTeam();
			}else{
				Toast.makeText(this, "团队名不能为空!", 0).show();
			}
		}else{
			Toast.makeText(this, "团队ID不能为空!", 0).show();
		}
	}
	/**
	 * 发送http请求，创建团队
	 */
	private void createTeam() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				teamCreater = sp.getString("account", null);
				String teamCreaterName = sp.getString("nickname", null);
				Log.i("tag", teamCreaterName);
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/createTeam");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("teamID", et_createTeam_teamID.getText().toString()));
					params.add(new BasicNameValuePair("teamName", et_createTeam_teamName.getText().toString()));
					params.add(new BasicNameValuePair("teamCreater", teamCreater));
					params.add(new BasicNameValuePair("teamCreaterName", teamCreaterName));
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
