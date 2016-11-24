package com.lzz.officeassistant.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class OrderMeetingRoomActivity extends Activity implements View.OnTouchListener{
	private EditText et_orderMR_topic;
	private EditText et_orderMR_startTime;
	private EditText et_orderMR_endTime;
	private String [] timeArray;
	private String mRGID;
	private String name;
	private String orderTeam;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			if(response.equals("预约成功!")){
				Toast.makeText(OrderMeetingRoomActivity.this, "预约成功!", 0).show();
				onBackPressed();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_mr);
		
		SharedPreferences sp = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
		String currentUser = sp.getString("account", null);
		sp = getSharedPreferences(currentUser,Context.MODE_PRIVATE);
		orderTeam = sp.getString("teamID", null);
		
		et_orderMR_topic = (EditText) findViewById(R.id.et_orderMR_topic);
		et_orderMR_startTime = (EditText) findViewById(R.id.et_orderMR_startTime);
		et_orderMR_endTime = (EditText)findViewById(R.id.et_orderMR_endTime);
		Button b_orderMR_order = (Button) findViewById(R.id.b_orderMR_order);
		
		Intent i = getIntent();
		mRGID = i.getStringExtra("mRGID");
		name = i.getStringExtra("name");
		String time = i.getStringExtra("time");
		System.out.println(time);
		timeArray = time.split("\\.");

		b_orderMR_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_orderMR_topic.getText().toString().equals("")){
					if(!et_orderMR_startTime.getText().toString().equals("")&&!et_orderMR_endTime.getText().toString().equals("")){
						String startTime = et_orderMR_startTime.getText().toString();
						String endTime = et_orderMR_endTime.getText().toString();
						SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date d1 = null;
						Date d2 = null;
						Date now = new Date();
						try {
							d1 = s.parse(startTime);
							d2 = s.parse(endTime);
							now = s.parse(s.format(now));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(d1.getTime()<d2.getTime()){
							if(d1.getTime()>=now.getTime()){
								int counter = 0;
								Date start = null;
								Date end = null;
								for(int i =0;i<timeArray.length/2;i++){
									try {
										start = s.parse(timeArray[2*i]);
										end = s.parse(timeArray[2*i+1]);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									if(d1.getTime()>=end.getTime()||d2.getTime()<=start.getTime()){
										counter ++;
									}
								}
								if(counter == timeArray.length/2){
									orderMeetingRoom();
									
									
								}else{
									Toast.makeText(OrderMeetingRoomActivity.this, "预定会议时间有冲突!", 0).show();
								}
							}else{
								Toast.makeText(OrderMeetingRoomActivity.this, "开始时间不应早于现在!", 0).show();
							}
						}else{
							Toast.makeText(OrderMeetingRoomActivity.this, "开始时间应该早于结束时间!", 0).show();
						}
					}else{
						Toast.makeText(OrderMeetingRoomActivity.this, "会议时间不能为空!", 0).show();
					}
				}else{
					Toast.makeText(OrderMeetingRoomActivity.this, "会议标题不能为空!", 0).show();
				}
			}
		});
		
		et_orderMR_startTime.setOnTouchListener(this);
		et_orderMR_endTime.setOnTouchListener(this);
		
		
		
	}
	
	 @Override
	public boolean onTouch(View v, MotionEvent event) {
	     if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	         View view = View.inflate(this, R.layout.data_time_dialog, null);
	            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
	            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
	            builder.setView(view);
	 
	            Calendar cal = Calendar.getInstance();
	            cal.setTimeInMillis(System.currentTimeMillis());
	            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
	 
	            timePicker.setIs24HourView(true);
	            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
	            timePicker.setCurrentMinute(Calendar.MINUTE);
	 
	            if (v.getId() == R.id.et_orderMR_startTime) {
	                final int inType = et_orderMR_startTime.getInputType();
	                et_orderMR_startTime.setInputType(InputType.TYPE_NULL);
	                et_orderMR_startTime.onTouchEvent(event);
	                et_orderMR_startTime.setInputType(inType);
	                et_orderMR_startTime.setSelection(et_orderMR_startTime.getText().length());
	 
	                builder.setTitle("选取起始时间");
	                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
	 
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	 
	                        StringBuffer sb = new StringBuffer();
	                        sb.append(String.format("%d-%02d-%02d",
	                                datePicker.getYear(),
	                                datePicker.getMonth() + 1,
	                                datePicker.getDayOfMonth()));
	                        sb.append(" ");
	                        sb.append(String.format("%02d:%02d",
	                        		timePicker.getCurrentHour(),
	                        		timePicker.getCurrentMinute()));
	                        et_orderMR_startTime.setText(sb);
	                        et_orderMR_endTime.requestFocus();
	 
	                        dialog.cancel();
	                    }
	                });
	 
	            } else if (v.getId() == R.id.et_orderMR_endTime) {
	                int inType = et_orderMR_endTime.getInputType();
	                et_orderMR_endTime.setInputType(InputType.TYPE_NULL);
	                et_orderMR_endTime.onTouchEvent(event);
	                et_orderMR_endTime.setInputType(inType);
	                et_orderMR_endTime.setSelection(et_orderMR_endTime.getText().length());
	 
	                builder.setTitle("选取结束时间");
	                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
	 
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	 
	                        StringBuffer sb = new StringBuffer();
	                        sb.append(String.format("%d-%02d-%02d",
	                                datePicker.getYear(),
	                                datePicker.getMonth() + 1,
	                                datePicker.getDayOfMonth()));
	                        sb.append(" ");
	                        sb.append(String.format("%02d:%02d",
	                        		timePicker.getCurrentHour(),
	                        		timePicker.getCurrentMinute()));
	                        et_orderMR_endTime.setText(sb);
	 
	                        dialog.cancel();
	                    }
	                });
	            }
	 
	            Dialog dialog = builder.create();
	            dialog.show();
	        }
	 
	        return true;
	    }
	 
	 private void orderMeetingRoom(){
		 new Thread(new Runnable(){
				@Override
				public void run() {
					HttpClient httpClient=null;
					try{
						httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/OrderMeetingRoom");
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("mRGID",mRGID));
						params.add(new BasicNameValuePair("name", name));
						params.add(new BasicNameValuePair("topic", et_orderMR_topic.getText().toString()));
						params.add(new BasicNameValuePair("orderTeam", orderTeam));
						params.add(new BasicNameValuePair("startTime", et_orderMR_startTime.getText().toString()));
						params.add(new BasicNameValuePair("endTime", et_orderMR_endTime.getText().toString()));
						System.out.println("dd"+et_orderMR_startTime.getText().toString().length());
						UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
						httpPost.setEntity(entry);
						HttpResponse httpResponse = httpClient.execute(httpPost);
						if(httpResponse.getStatusLine().getStatusCode() == 200){
							HttpEntity entity = httpResponse.getEntity();
							String response = EntityUtils.toString(entity,"utf-8");
							Message message = new Message();
							message.what = 0;
							message.obj = response;
							handler.sendMessage(message);
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
