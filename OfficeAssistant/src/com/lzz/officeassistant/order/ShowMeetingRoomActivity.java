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
import org.json.JSONArray;
import org.json.JSONObject;

import com.lzz.officeassistant.R;
import com.lzz.officeassistant.review.ReviewAdapter;
import com.lzz.officeassistant.review.ReviewDetailActivity;
import com.lzz.officeassistant.review.ReviewHandleOverActivity;
import com.lzz.officeassistant.review.ReviewInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowMeetingRoomActivity extends Activity {
	private String mRGID;
	
	private Handler handler = new Handler(){
		private List<MeetingRoom> mrArray = new ArrayList<MeetingRoom>();
		@Override
		public void handleMessage(Message msg) {
			mrArray = (List<MeetingRoom>) msg.obj;
			MeetingRoomAdapter adapter = new MeetingRoomAdapter(ShowMeetingRoomActivity.this, R.layout.item_rm,mrArray);
			ListView lv_show_mr = (ListView) findViewById(R.id.lv_show_mr);
			lv_show_mr.setAdapter(adapter);
			lv_show_mr.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					MeetingRoom mr = mrArray.get(position);
					Bundle bundle = new Bundle();
					bundle.putSerializable("mr", mr);
					Intent i = new Intent(view.getContext(),ShowMeetingRoomDetailActivity.class);
					i.putExtras(bundle);
					startActivity(i);
				}
			});
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_show_mr);
		
		mRGID = getIntent().getStringExtra("mRGID");
		ShowMeetingRoom();
	}
	private void ShowMeetingRoom(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ShowMeetingRoom");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("mRGID", mRGID));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						System.out.println("gghh  "+response);
						JSONArray ja = new JSONArray(response);
						JSONObject jo = null;
						List<MeetingRoom> mrArray = new ArrayList<MeetingRoom>();
						MeetingRoom mr = null;
						for(int i=0;i<ja.length();i++){
							jo = ja.getJSONObject(i);
							mr=new MeetingRoom(jo.getString("name"),mRGID,jo.getString("location"),jo.getString("floor"),
									jo.getString("accommodate"),jo.getString("wifi"),jo.getString("projector"),jo.getString("othersSupport"));
							mrArray.add(mr);
						}
						Message message = new Message();
						message.obj = mrArray;
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
