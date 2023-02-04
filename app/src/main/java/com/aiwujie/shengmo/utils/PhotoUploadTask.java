package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoUploadTask extends Thread {

	URL url;
	String str, boundary;
	InputStream is;
	String res,user_id, user_token,user_type;
	Message msg;
	Context context;
	Handler handler;

	public static final int CODE_UPLOAD_SUC = 152;

	public PhotoUploadTask(String str, InputStream is, Context context,
						   Handler handler) {
		super();
		this.str = str;
		this.user_id = "0";
		this.user_token = "1";
		this.is = is;
		this.user_type = "1";
		this.handler = handler;
		this.context = context;
	}
	int index = -1;
	public PhotoUploadTask(String str, InputStream is, Context context,int index,
						   Handler handler) {
		super();
		this.str = str;
		this.user_id = "0";
		this.user_token = "1";
		this.is = is;
		this.user_type = "1";
		this.handler = handler;
		this.index = index;
		this.context = context;
	}

	@Override
	public void run() {
		super.run();
		boundary = "----WebKitFormBoundaryzrfXlGEP1Y8Qmzxf";
		try {
			url = new URL(str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			//控制返回数据乱码，如果乱码就注释掉
//			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,lzma,sdch");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("Cache-Control", "max-age=0");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			conn.setRequestProperty("Cookie", "JSESSIONID=aaaj_rgTbZT8o-BVGu2yu");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.132 Safari/537.36 OPR/21.0.1432.67");
			conn.connect();
			OutputStream os = conn.getOutputStream();
			StringBuilder sb = new StringBuilder();
//			sb.append("--");
//			sb.append(boundary);
//			sb.append("\r\n");
//			sb.append("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n");
//			sb.append("Content-Type: image/jpeg\r\n\r\n");

			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n");

			sb.append(user_id);
			sb.append("\r\n");
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"user_token\"\r\n\r\n");

			sb.append(user_token);
			sb.append("\r\n");
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"user_type\"\r\n\r\n");

			sb.append(user_type);
			sb.append("\r\n");
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"test.jpg\"\r\n");
			sb.append("Content-Type: image/jpeg\r\n\r\n");

			os.write(sb.toString().getBytes());
			byte[] bs = new byte[1024];
			int len = 0;
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			is.close();
			os.write("\r\n".getBytes());
			os.write(("--" + boundary + "--").getBytes());
			os.flush();

			BufferedReader input = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine);
			}
			res = response.toString();
			Message message = handler.obtainMessage();
			message.obj = res;
			if (index != -1) {
				message.arg1 = index;
			}
			message.what = CODE_UPLOAD_SUC;
			handler.sendMessage(message);
			// System.out.println("上传图片返回数据  : " + res);

			// {"retcode":2000,"msg":"上传成功","data":"Uploads\/Picture\/2016-04-26\/2016042615112262.jpg"}

			// JSONObject object = newsrdz JSONObject(res);
			// msg = newsrdz Message();
			// if (object.getInt("retcode") == 2000) {
			// msg.what = 121;
			//
			// } else if (object.getInt("retcode") == 400) {
			// System.out.println("上传图片返回数据状态值11111111111 : " +
			// object.getInt("retcode"));
			//
			// msg.what = 101;
			// } else {
			// msg.what = 131;
			// }
			// msg.obj = object.getString("msg");
			// System.out.println("上传图片返回数据信息: " + object.getString("msg"));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
