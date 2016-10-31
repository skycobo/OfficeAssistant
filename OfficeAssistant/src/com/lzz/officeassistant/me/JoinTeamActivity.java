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

public class JoinTeamActivity extends Activity {
	private EditText et_joinTeam_teamID;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String)msg.obj;
			if(response.substring(0, 6).equals("该团队不存在")){
				Toast.makeText(JoinTeamActivity.this, response, 0).show();
			}else{
				Toast.makeText(JoinTeamActivity.this, response, 0).show();
				onBackPressed();
				
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_team);
		et_joinTeam_teamID = (EditText) findViewById(R.id.et_joinTeam_teamID);
		Button b_joinTeam = (Button) findViewById(R.id.b_joinTeam);
		b_joinTeam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_joinTeam_teamID.getText().toString().equals("")){
					applyForJionTeam();
				}else{
					Toast.makeText(JoinTeamActivity.this, "团队ID不能为空!", 0).show();
				}
				
			}

			
		});
	}
	private void applyForJionTeam() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				String account=sp.getString("account","");
				String nickname = sp.getString("nickname", "");
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/applyForJoinTeam");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("teamID", et_joinTeam_teamID.getText().toString()));
					params.add(new BasicNameValuePair("account",account));
					params.add(new BasicNameValuePair("nickname",nickname));
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
