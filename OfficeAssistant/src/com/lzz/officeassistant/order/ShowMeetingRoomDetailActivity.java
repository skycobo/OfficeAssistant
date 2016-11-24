package com.lzz.officeassistant.order;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowMeetingRoomDetailActivity extends Activity {
	private MeetingRoom mr;
	private String time;
	private TextView tv_mrDetail_teamAndTime;
	private Handler handler = new Handler(){
		private List<Meeting> meetingsArray;
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				meetingsArray =  (List<Meeting>) msg.obj;
				String teamAndTime = null;
				String allTeamAndTime = "";
				for(int i = 0;i<meetingsArray.size()-1;i++){
					teamAndTime = (i+1)+". "+meetingsArray.get(i).getStartTime()+"至"+meetingsArray.get(i).getEndTime()
							+"("+meetingsArray.get(i).getOrderTeam()+")\n";
					allTeamAndTime += teamAndTime;
				}
				teamAndTime = meetingsArray.size()+". "+meetingsArray.get(meetingsArray.size()-1).getStartTime()+"至"+meetingsArray.get(meetingsArray.size()-1).getEndTime()
						+"("+meetingsArray.get(meetingsArray.size()-1).getOrderTeam()+")";
				allTeamAndTime += teamAndTime;
				tv_mrDetail_teamAndTime.setText(allTeamAndTime);
				
				
				String regex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(allTeamAndTime);
				time = "";
				while(m.find()){
					System.out.println(m.group());
					time = time + m.group()+".";
				}
				
			}else{
				tv_mrDetail_teamAndTime.setText("暂无预约!");
			}
		}
		
	};
	
	@Override
	protected void onRestart() {
		showMeetings();
		super.onRestart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_mr_detail);
		
		TextView tv_mrDetail_name = (TextView) findViewById(R.id.tv_mrDetail_name);
		TextView tv_mrDetail_location = (TextView) findViewById(R.id.tv_mrDetail_location);
		TextView tv_mrDetail_floor = (TextView) findViewById(R.id.tv_mrDetail_floor);
		TextView tv_mrDetail_accommodate = (TextView) findViewById(R.id.tv_mrDetail_accommodate);
		TextView tv_mrDetail_wifi = (TextView) findViewById(R.id.tv_mrDetail_wifi);
		TextView tv_mrDetail_projector = (TextView) findViewById(R.id.tv_mrDetail_projector);
		TextView tv_mrDetail_othersSupport = (TextView) findViewById(R.id.tv_mrDetail_othersSupport);
		tv_mrDetail_teamAndTime = (TextView) findViewById(R.id.tv_mrDetail_teamtime);
		Button b_orderMR = (Button) findViewById(R.id.b_orderMR);
		
		Intent i = getIntent();
		mr = (MeetingRoom) i.getSerializableExtra("mr");
		
		tv_mrDetail_name.setText("会议室名称:"+mr.getName());
		tv_mrDetail_location.setText("位置:"+mr.getLocation());
		tv_mrDetail_floor.setText("楼层:"+mr.getFloor());
		tv_mrDetail_accommodate.setText("容纳人数:"+mr.getAccommodate());
		tv_mrDetail_wifi.setText("支持WIFI:"+mr.getWifi());
		tv_mrDetail_projector.setText("支持投影仪:"+mr.getProjector());
		tv_mrDetail_othersSupport.setText("其他支持:"+mr.getOthersSupport());
		showMeetings();
		
		b_orderMR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ShowMeetingRoomDetailActivity.this,OrderMeetingRoomActivity.class);
				i.putExtra("time", time);
				i.putExtra("mRGID", mr.getmRGID());
				i.putExtra("name", mr.getName());
				startActivity(i);
			}
		});
	}
	
	private void showMeetings(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ShowMeetings");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("mRGID",mr.getmRGID()));
					params.add(new BasicNameValuePair("name", mr.getName()));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						if(response.equals("暂无预约!")){
							Message message = new Message();
							message.what = 0;
							message.obj = response;
							handler.sendMessage(message);
						}else{
							JSONArray ja = new JSONArray(response);
							JSONObject jo = null;
							List<Meeting> meetingsArray = new ArrayList<Meeting>();
							Meeting meeting = null;
							for(int i=0;i<ja.length();i++){
								jo = ja.getJSONObject(i);
								meeting=new Meeting(jo.getString("orderTeam"),jo.getString("startTime"),jo.getString("endTime"));
								meetingsArray.add(meeting);
							}
							Message message = new Message();
							message.what = 1;
							message.obj = meetingsArray;
							handler.sendMessage(message);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(httpClient!=null){
						httpClient.getConnectionManager().shutdown();
					}
				}
			}
			}).start();
	}
}
	



