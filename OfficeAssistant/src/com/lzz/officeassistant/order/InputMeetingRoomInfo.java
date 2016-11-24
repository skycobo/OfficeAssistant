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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class InputMeetingRoomInfo extends Activity{
	private MeetingRoom mr;
	private EditText et_meetingroom_name;
	private EditText et_meetingroom_location;
	private EditText et_meetingroom_floor;
	private EditText et_meetingroom_accommodate;
	private EditText et_meetingroom_othersSupport;
	private CheckBox cb_meetingroom_wifi;
	private CheckBox cb_meetingroom_projector;
	private String mRGID;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			if(response.equals("录入成功!")){
				Toast.makeText(InputMeetingRoomInfo.this, "录入成功!", 0).show();
			}else{
				Toast.makeText(InputMeetingRoomInfo.this, "该会议室已存在!", 0).show();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_rmi_input);
		
		Intent i = getIntent();
		mRGID = i.getStringExtra("mRGID");
		
		et_meetingroom_name = (EditText) findViewById(R.id.et_meetingroom_name);
		et_meetingroom_location = (EditText) findViewById(R.id.et_meetingroom_location);
		et_meetingroom_floor = (EditText) findViewById(R.id.et_meetingroom_floor);
		et_meetingroom_accommodate = (EditText) findViewById(R.id.et_meetingroom_accommodate);
		et_meetingroom_othersSupport = (EditText) findViewById(R.id.et_meetingroom_othersSupport);
		cb_meetingroom_wifi = (CheckBox) findViewById(R.id.cb_meetingroom_wifi);
		cb_meetingroom_projector = (CheckBox) findViewById(R.id.cb_meetingroom_projector);
		
		Button b_meetingroom_confirm = (Button) findViewById(R.id.b_meetingroom_confirm);
		b_meetingroom_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(et_meetingroom_name.getText().toString().equals("")||et_meetingroom_location.getText().toString().equals("")
						||et_meetingroom_floor.getText().toString().equals("")||et_meetingroom_accommodate.getText().toString().equals("")){
					Toast.makeText(InputMeetingRoomInfo.this, "请完善信息!", 0).show();
				}else{
					Intent i = getIntent();
					mr = new MeetingRoom();
					mr.setName(et_meetingroom_name.getText().toString());
					mr.setmRGID(mRGID);
					mr.setLocation(et_meetingroom_location.getText().toString());
					mr.setFloor(et_meetingroom_floor.getText().toString());
					mr.setAccommodate(et_meetingroom_accommodate.getText().toString());
					if(cb_meetingroom_wifi.isChecked()){
						mr.setWifi("yes");
					}else{
						mr.setWifi("no");
					}
					if(cb_meetingroom_projector.isChecked()){
						mr.setProjector("yes");
					}else{
						mr.setProjector("no");
					}
					mr.setOthersSupport(et_meetingroom_othersSupport.getText().toString());
					AlertDialog.Builder adb = new AlertDialog.Builder(InputMeetingRoomInfo.this);
					adb.setMessage("确定提交信息?");
					adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							uploadMRInfo();
							finish();
							Intent i = new Intent(InputMeetingRoomInfo.this,InputMeetingRoomInfo.class);
							startActivity(i);
						}
					});
					adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					adb.show();
					
				}
			}
		});
	}
	private void uploadMRInfo(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/UploadMRInfo");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("name", mr.getName()));
					params.add(new BasicNameValuePair("mRGID", mr.getmRGID()));
					params.add(new BasicNameValuePair("location", mr.getLocation()));
					params.add(new BasicNameValuePair("floor", mr.getFloor()));
					params.add(new BasicNameValuePair("accommodate", mr.getAccommodate()));
					params.add(new BasicNameValuePair("othersSupport", mr.getOthersSupport()));
					params.add(new BasicNameValuePair("wifi", mr.getWifi()));
					params.add(new BasicNameValuePair("projector", mr.getProjector()));
					
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
