package com.lzz.officeassistant.review;

import com.lzz.officeassistant.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ReviewFragment extends Fragment {
	private View view;
	
	@Override
	public void onStart() {
		TextView title_text = (TextView) view.findViewById(R.id.title_text);
		title_text.setText("审批");
		Button title_add=(Button) view.findViewById(R.id.title_add);
		title_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(view.getContext(),EditReviewActivity.class);
				view.getContext().startActivity(i);
			}
		});
		super.onStart();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_review_requester, null);
		return view;
	}

}
