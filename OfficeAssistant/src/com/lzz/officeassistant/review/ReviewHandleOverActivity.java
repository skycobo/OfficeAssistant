package com.lzz.officeassistant.review;

import java.io.BufferedReader;

import java.io.InputStreamReader;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class ReviewHandleOverActivity extends Activity {
	
	private Handler hl = new Handler(){
		List<ReviewInfo> reviewArray = new ArrayList<ReviewInfo>();
		@Override
		public void handleMessage(Message msg) {
				this.reviewArray = (List<ReviewInfo>) msg.obj;
				ReviewAdapter adapter = new ReviewAdapter(ReviewHandleOverActivity.this, R.layout.item_review,reviewArray);
				ListView lv_review_ho = (ListView) findViewById(R.id.lv_review_ho);
				lv_review_ho.setAdapter(adapter);
				lv_review_ho.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						ReviewInfo ri = reviewArray.get(position);
						Bundle bundle = new Bundle();
						bundle.putSerializable("review", ri);
						Intent i = new Intent(view.getContext(),ReviewDetailActivity.class);
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
		setContentView(R.layout.activity_review_handleover);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				SharedPreferences sp = getSharedPreferences("currentUser",Context.MODE_PRIVATE);
				String currentUser = sp.getString("account", null);
				sp = getSharedPreferences(currentUser,Context.MODE_PRIVATE);
				String handler = sp.getString("nickname", null)+"("+sp.getString("account", null)+")";
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ShowHandleOverReview");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("handler", handler));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						System.out.println("hhh"+response);
						JSONArray ja = new JSONArray(response);
						JSONObject jo = null;
						List<ReviewInfo> reviewArray = new ArrayList<ReviewInfo>();
						ReviewInfo ri = null;
						for(int i=0;i<ja.length();i++){
							jo = ja.getJSONObject(i);
							ri=new ReviewInfo(jo.getString("reviewID"),jo.getString("reviewTitle"),jo.getString("reviewType"),jo.getString("reviewContent"),
									jo.getString("commitTime"),jo.getString("requester"),jo.getString("handler"),jo.getString("status"),
									jo.getString("reviewIdea"),jo.getString("responseTime"));
							reviewArray.add(ri);
						}
						Message message = new Message();
						message.what = 0;
						message.obj = reviewArray;
						hl.sendMessage(message);
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
