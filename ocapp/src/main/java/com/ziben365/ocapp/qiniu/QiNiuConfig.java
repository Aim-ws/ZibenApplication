package com.ziben365.ocapp.qiniu;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;

/**
 * Created by lixiaodaoaaa on 14/10/12.
 *
 * 七牛配置文件
 */
public final class QiNiuConfig {
	public static final String token = getToken();
	public static final String QINIU_AK = "VV_9nrrkrdOm0SG6kxMMTQV4-o4H5ePyaxujpukn";
	public static final String QINIU_SK = "FIudUOIodc_vCX4Rb5Aq_CgAPott7gJl2hmwgi6V";
	public static final String QINIU_BUCKNAME = "zibenapp";
	
	public static final String ZIBEN_KEY = "upload";
	
	public static final String QINIU_PIC_URL = "http://dn-appziben.qbox.me/";
	public static final String QINIU_PROJECT_URL = "http://dn-ziben.qbox.me/";

	public static String getToken() {
		Mac mac = new Mac(QiNiuConfig.QINIU_AK, QiNiuConfig.QINIU_SK);
		PutPolicy putPolicy = new PutPolicy(QiNiuConfig.QINIU_BUCKNAME);
		putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
		try {
			String uptoken = putPolicy.token(mac);
			System.out.println("debug:uptoken = " + uptoken);
			return uptoken;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
