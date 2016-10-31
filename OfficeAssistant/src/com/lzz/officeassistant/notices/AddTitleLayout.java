package com.lzz.officeassistant.notices;

import com.lzz.officeassistant.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class AddTitleLayout extends LinearLayout {

	public AddTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.add_title, this);
	}

}
