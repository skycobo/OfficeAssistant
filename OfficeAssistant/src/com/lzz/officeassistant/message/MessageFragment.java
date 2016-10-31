package com.lzz.officeassistant.message;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageFragment extends Fragment {
	private View view;
	private List<String> afjtArray;
	private String [] data;
	private String account;
	private String nickname;
	private SharedPreferences sp = null;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.obj!=null){
				afjtArray = (List<String>) msg.obj;
				data = new String[afjtArray.size()];
				for(int i=0;i<afjtArray.size();i++){
					data[i]=afjtArray.get(i);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1, data);
				ListView lv_message_applyInfo = (ListView) view.findViewById(R.id.lv_message_applyInfo);
				lv_message_applyInfo.setAdapter(adapter);
				lv_message_applyInfo.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String user = data[position];
						account=user.substring(user.indexOf("(")+1, user.indexOf(")"));
						nickname = user.substring(5,user.indexOf("("));
						AlertDialog.Builder ab = new Builder(getActivity());
						ab.setMessage("同意该用户加入团队吗?");
						ab.setNegativeButton("拒绝", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								denyAFJT();
							}
						});
						ab.setPositiveButton("同意", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								agreeAFJT();
							}
						});
						ab.show();
					}
				});
			}
		}
		
	};
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
	}

	
	@Override
	public void onStart() {
		sp=view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		TextView tv_message_groupChat = (TextView) view.findViewById(R.id.tv_message_groupChat);
		tv_message_groupChat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(view.getContext(),GroupChatActivity.class);
				startActivity(i);
				
			}
		});
		if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
			queryAFJT();
		}
		super.onStart();
	}
	private void agreeAFJT(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				String teamID = sp.getString("teamID", null);
				String teamName = sp.getString("teamName", null);
				String teamCreater = sp.getString("teamCreater", null);
				String teamCreaterName = sp.getString("nickname", null);
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/AFJTeamResponse");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("operation", "agree"));
					params.add(new BasicNameValuePair("teamID",teamID));
					params.add(new BasicNameValuePair("teamName", teamName));
					params.add(new BasicNameValuePair("teamCreater", teamCreater));
					params.add(new BasicNameValuePair("teamCreaterName", teamCreaterName));
					Log.i("tag", account+"---"+nickname+"----"+teamCreaterName);
					params.add(new BasicNameValuePair("member", account));
					params.add(new BasicNameValuePair("memberName", nickname));
					
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
			}
		}
		).start();
	}
	private void denyAFJT(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/AFJTeamResponse");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("operation", "deny"));
					Log.i("tag", "----"+account);
					params.add(new BasicNameValuePair("account", account));
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
			}
		}
		).start();
	}


	private void queryAFJT() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient = null;
				SharedPreferences sp = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				String teamID = sp.getString("teamID", null);
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/AFJTeamResponse");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("operation", "query"));
					params.add(new BasicNameValuePair("teamID", teamID));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						parseAFJT(response);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(httpClient!=null){
						httpClient.getConnectionManager().shutdown();
					}
				}
			}
		}
		).start();
		
		
	}
	private void parseAFJT(String jsonData) {
		List<String> afjtInfo = new ArrayList<String>();
		try {
			JSONArray ja = new JSONArray(jsonData);
			JSONObject jo = null;
			for(int i = 0;i<jsonData.length();i++){
				jo = ja.getJSONObject(i);
				afjtInfo.add("消息提示:"+jo.getString("nickname")+"("+jo.getString("account")+")请求加入团队!");
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		Message message = new Message();
		if(afjtInfo!=null){
			message.obj=afjtInfo;
			handler.sendMessage(message);
		}
		
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_message, null);
		return view;
	}
}
