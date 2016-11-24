package com.lzz.officeassistant.review;

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

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewFragment extends Fragment {
	private View view;
	private SharedPreferences sp;
	
	private Handler hl = new Handler(){
		List<ReviewInfo> reviewArray = new ArrayList<ReviewInfo>();
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				this.reviewArray = (List<ReviewInfo>) msg.obj;
				ReviewAdapter adapter = new ReviewAdapter(view.getContext(), R.layout.item_review,reviewArray);
				ListView lv_review_handler_wait = (ListView) view.findViewById(R.id.lv_review_handler_wait);
				lv_review_handler_wait.setAdapter(adapter);
				lv_review_handler_wait.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						ReviewInfo ri = reviewArray.get(position);
						Bundle bundle = new Bundle();
						bundle.putSerializable("review", ri);
						Intent i = new Intent(view.getContext(),ReviewHandleActivity.class);
						i.putExtras(bundle);
						startActivity(i);
					}
				});
				break;
			case 1:
				this.reviewArray = (List<ReviewInfo>) msg.obj;
				ReviewAdapter adapter1 = new ReviewAdapter(view.getContext(),R.layout.item_review, reviewArray);
				ListView lv_review_requester_wait = (ListView) view.findViewById(R.id.lv_review_requester_wait);
				lv_review_requester_wait.setAdapter(adapter1);
				lv_review_requester_wait.setOnItemClickListener(new OnItemClickListener() {
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
				break;
			}
		}
	};
	
	@Override
	public void onStart() {
		if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
			showHandlerReview();
			Button b_rh_over = (Button) view.findViewById(R.id.b_rh_over);
			b_rh_over.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(v.getContext(),ReviewHandleOverActivity.class);
					v.getContext().startActivity(i);
					
				}
			});
		}else{
			TextView title_text = (TextView) view.findViewById(R.id.title_text);
			title_text.setText("审批");
			Button title_add=(Button) view.findViewById(R.id.title_add);
			title_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(view.getContext(),ReviewEditActivity.class);
					view.getContext().startActivity(i);
				}
			});
			showRequesterReview();
			
		}
		super.onStart();
	}
	public void showRequesterReview(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String requester = sp.getString("nickname", null)+"("+sp.getString("account", null)+")";
				System.out.println(requester);
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ShowRequesterReview");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("requester", requester));
					UrlEncodedFormEntity entry = new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entry);
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						System.out.println("ggg"+response);
						JSONArray ja = new JSONArray(response);
						JSONObject jo = null;
						List<ReviewInfo> reviewArray = new ArrayList<ReviewInfo>();
						ReviewInfo ri = null;
						for(int i=0;i<ja.length();i++){
							jo = ja.getJSONObject(ja.length()-i-1);
							ri=new ReviewInfo(jo.getString("reviewID"),jo.getString("reviewTitle"),jo.getString("reviewType"),jo.getString("reviewContent"),
									jo.getString("commitTime"),jo.getString("requester"),jo.getString("handler"),jo.getString("status"),
									jo.getString("reviewIdea"),jo.getString("responseTime"));
							reviewArray.add(ri);
						}
					    ri = reviewArray.get(0);
					    String reviewCounter = ri.getReviewID().substring(ri.getReviewID().indexOf("(")+1, ri.getReviewID().indexOf(")"));
						Integer counter = Integer.parseInt(reviewCounter);
						if(counter!=sp.getInt("reviewCounter", 0)){
						    Editor edit = sp.edit();
						    edit.putInt("reviewCounter", counter);
						    edit.commit();
						}
						System.out.println("reviewCounter:"+reviewCounter);
					    Message message = new Message();
					    message.what =1;
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
	
	private void showHandlerReview(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String handler = sp.getString("nickname", null)+"("+sp.getString("account", null)+")";
				HttpClient httpClient=null;
				try{
					httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://www.skycobo.com:8080/OfficeAssistantServer/ShowHandlerReview");
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
		
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_review_requester, null);
		sp = view.getContext().getSharedPreferences("currentUser", Context.MODE_PRIVATE);
		String currentUser = sp.getString("account", null);
		sp = view.getContext().getSharedPreferences(currentUser,Context.MODE_PRIVATE);
		if(sp.getString("account", null).equals(sp.getString("teamCreater", null))){
			view =inflater.inflate(R.layout.fragment_review_handler, null);
		}
		return view;
	}
	

}
