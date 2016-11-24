package com.lzz.officeassistant.signin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.lzz.officeassistant.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SignInFragment extends Fragment {
	private View view;
	private ListView lv_signin_list;
	private LocationManager locationManage;
	private String provider;
	private String position;
	private List signInInfoArray;
	private SharedPreferences sp = null;
	private SharedPreferences sp1 =null;
	private Date date  = null;
	private SimpleDateFormat dateFm = null;
	
	private Handler handler = new Handler(){
		String currentPosition =null;
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				currentPosition = (String)msg.obj;
				position = currentPosition;
				AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
				adb.setTitle("签到信息");
				adb.setMessage(currentPosition);
				adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						uploadSignInInfo();
					}
				});
				adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				adb.show();
				break;
			case 1:
				signInInfoArray = (List<SignInInfo>) msg.obj;
				SignInAdapter adapter = new SignInAdapter(view.getContext(), R.layout.item_signin,signInInfoArray);
				lv_signin_list = (ListView) view.findViewById(R.id.lv_signin_list);
				lv_signin_list.setAdapter(adapter);
			}
		}
	};
	@Override
	public void onStart() {
		sp =view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
	    sp1 = view.getContext().getSharedPreferences("signin", Context.MODE_APPEND);
		
		TextView tv_signin_time = (TextView) view.findViewById(R.id.tv_signin_time);
		date = new Date();
		dateFm = new SimpleDateFormat("yyyy年MM月dd日\nEEEE");
		if(sp1.getString("day", null)==null){
			Editor editor = sp1.edit();
			editor.putString("day", dateFm.format(date).substring(0, 11));
			editor.commit();
		}else{
			if(!sp1.getString("day", null).equals(dateFm.format(date).substring(0, 11))){
				Editor editor = sp1.edit();
				editor.putString("day", dateFm.format(date).substring(0, 11));
				editor.putBoolean("day", false);
				editor.commit();
			}
		}
		tv_signin_time.setText(dateFm.format(date));
		Button b_signin = (Button) view.findViewById(R.id.b_signin);
		b_signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(sp.getString("teamID", null)!=null){
					if(sp1.getBoolean("flag",false)){
						Toast.makeText(getActivity(), "你今天已签到了!", 0).show();
					}else{
						signin();
						Editor editor = sp1.edit();
						editor.putBoolean("flag", true);
						editor.commit();
					}
				}else{
					Toast.makeText(getActivity(), "你还未找到组织呢!", 0).show();
				}
				
			}
		});
		showSignInInfo();
		super.onStart();
	}
	private void showSignInInfo(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String teamID = sp.getString("teamID", null);
				HttpClient hc = null;
				try{
					hc = new DefaultHttpClient();
					HttpPost hp = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/showSignInInfo");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("teamID", teamID));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					hp.setEntity(entry);
					HttpResponse hs = hc.execute(hp);
					if(hs.getStatusLine().getStatusCode()==200){
						HttpEntity entity = hs.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						parseSignInInfo(response);
					
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(hc!=null){
						hc.getConnectionManager().shutdown();
					}
				}
				
			}}).start();
	}
	private void parseSignInInfo(String parseStr){
		List<SignInInfo> siiList = new ArrayList<SignInInfo>();
		SignInInfo sii = null;
		String [] strArray = parseStr.split("\\n");
		for(String s: strArray){
			String [] subStrArray =s.split(" ");
			sii = new SignInInfo(subStrArray[3], subStrArray[2],subStrArray[1]);
			siiList.add(sii);
		}
		Message message = new Message();
		message.what = 1;
		message.obj =siiList;
		handler.sendMessage(message);
	}
	public void signin(){
		locationManage = (LocationManager)view.getContext().getSystemService(Context.LOCATION_SERVICE);
		
		List<String> providerList = locationManage.getProviders(true);
		if(providerList.contains(LocationManager.GPS_PROVIDER)){
			provider=LocationManager.GPS_PROVIDER;
		}else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
			provider=LocationManager.NETWORK_PROVIDER;
		}
		Location location = locationManage.getLastKnownLocation(provider);
		if(location!=null){
			showLocation(location);
		}
		locationManage.requestLocationUpdates(provider, 5000, 1, locationListener);
	}
	private void showLocation(final Location location){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					String url="http://api.map.baidu.com/geocoder/v2/?ak=ko2s0xvQ0UsxsGrVXli3oaI4lIt18bqC&"
							+ "mcode=C5:DD:88:29:48:B7:ED:BE:48:BC:42:4E:A0:B7:60:F3:61:D3:C5:0A;com.lzz.officeassistant&callback=renderReverse&location="
							+location.getLatitude()+","+location.getLongitude()+"&output=json";
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);
					httpGet.addHeader("Accept-Language","zh-CN");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode()==200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						String s = response.substring(response.indexOf("(")+1,response.indexOf(")") );
		
						JSONObject jo = new JSONObject(s);
						JSONObject jo1 = jo.getJSONObject("result");
						String address= jo1.getString("formatted_address");
						Log.i("tag",address);
						Message message = new Message();
						message.what = 0;
						message.obj = address;
						handler.sendMessage(message);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}).start();
	}
	LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
//			showLocation(location);
		}
	};
	private void uploadSignInInfo(){
		new Thread(new Runnable(){
			String teamID = sp.getString("teamID", null);
			String nickname = sp.getString("nickname", null);
			@Override
			public void run() {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm");
				String time = sdf.format(date);
				HttpClient hc = null;
				try{
					hc = new DefaultHttpClient();
					HttpPost hp = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/uploadSignInInfo");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("teamID", teamID));
					params.add(new BasicNameValuePair("nickname", nickname));
					params.add(new BasicNameValuePair("position", position));
					params.add(new BasicNameValuePair("time", time));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					hp.setEntity(entry);
					hc.execute(hp);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(hc!=null){
						hc.getConnectionManager().shutdown();
					}
				}
				
			}}).start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_signin, null);
		return view;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(locationManage!=null){
			locationManage.removeUpdates(locationListener);
		}
	}
}
