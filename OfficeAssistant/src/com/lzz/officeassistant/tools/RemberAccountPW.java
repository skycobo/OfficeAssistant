package com.lzz.officeassistant.tools;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class RemberAccountPW {
	/**
	 * 保存用户账号和密码到SharedPreferences
	 * @param account
	 * @param pw
	 * @return true 成功
	 */
	public static boolean saveUserInfo(Context context, String account, String pw) {
		try {
			SharedPreferences sp = context.getSharedPreferences("account_pw", Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("account", account);
			edit.putString("pw", pw);
			edit.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	/**
	 * 到SharedPreferences获取用户信息
	 * @return
	 */
	public static Map<String, String> getUserInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences("account_pw", Context.MODE_PRIVATE);
		String account = sp.getString("account", null);
		String pw = sp.getString("pw", null);
		if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pw)) {
			Map<String, String> userInfoMap = new HashMap<String, String>();
			userInfoMap.put("account", account);
			userInfoMap.put("pw", pw);
			return userInfoMap;
		}
		return null;
	}
}
