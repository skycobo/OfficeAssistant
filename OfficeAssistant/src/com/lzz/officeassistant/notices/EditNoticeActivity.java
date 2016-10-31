package com.lzz.officeassistant.notices;

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

import com.lzz.officeassistant.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoticeActivity extends Activity {
	private EditText et_editNotice_title;
	private EditText et_editNotice_content;
	private Button b_editNotice_publish;
	private Notice notice;
	private String JsonNotice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_notice);
		et_editNotice_title = (EditText) findViewById(R.id.et_editNotice_title);
		et_editNotice_content = (EditText) findViewById(R.id.et_editNotice_content);
		b_editNotice_publish = (Button) findViewById(R.id.b_editNotice_publish);
		b_editNotice_publish.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				check();
			}
		});

	}
	private void check() {
		if(!et_editNotice_title.getText().toString().equals("")){
			if(!et_editNotice_content.getText().toString().equals("")){
				File file = new File("/data/data/com.lzz.officeassistant/shared_prefs/notice_count.xml");
				if(file.exists()){
					Log.i("tag", "存在!");
					SharedPreferences sp = getSharedPreferences("notice_count", Context.MODE_PRIVATE);
					String id = sp.getString("count", "0");
					Editor edit = sp.edit();
					edit.putString("count",(Integer.valueOf(id)+1)+"");
					edit.commit();
					notice = new Notice(Integer.valueOf(id)+1,et_editNotice_title.getText().toString(),et_editNotice_content.getText().toString(),"2016.10.3");
					JsonNotice = ",{\"id\":"+"\""+notice.getId()+"\""+",\"title\":"+"\""+notice.getTitle()+"\""+",\"content\":"+"\""+notice.getContent()+"\""+",\"time\":"+"\""+notice.getTime()+"\""+"}]";
					sendNoticeToServer();
					}else{
						Log.i("tag", "不存在!");
						SharedPreferences sp = getSharedPreferences("notice_count", Context.MODE_PRIVATE);
						Editor edit = sp.edit();
						edit.putString("count", String.valueOf(1));
						edit.commit();
						notice = new Notice(1,et_editNotice_title.getText().toString(),et_editNotice_content.getText().toString(),"2016.10.3");
						JsonNotice = "[{\"id\":"+"\""+notice.getId()+"\""+",\"title\":"+"\""+notice.getTitle()+"\""+",\"content\":"+"\""+notice.getContent()+"\""+",\"time\":"+"\""+notice.getTime()+"\""+"}]";
						sendNoticeToServer();
					}
				onBackPressed();
			}else{
				Toast.makeText(this, "请输入内容!", 0).show();
			}
		}else{
			Toast.makeText(this, "请输入标题!", 0).show();
		}
		
	}

	private void sendNoticeToServer() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/addNotice");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("teamID", sp.getString("teamID", null)));
					params.add(new BasicNameValuePair("notice", JsonNotice));
					params.add(new BasicNameValuePair("noticeId", String.valueOf(notice.getId())));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
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
