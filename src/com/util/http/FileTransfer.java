package com.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.http.Header;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.util.Log;

public class FileTransfer {

	private static String serveUrl = "";

	private static FileTransfer instance = null;

	public static void SetServeUrl(final String url) {
		serveUrl = url;
	}
	public static FileTransfer get() {
		if (instance == null) {
			instance = new FileTransfer();
		}
		return instance;
	}

	public void download(final String downloadUrl, final String savePath,
			final DownloadCallBack callBack) {
		try {
			HttpClientUtil.getClient().get(downloadUrl,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] response, Throwable throwable) {
							callBack.failed("http错误:"+ statusCode + throwable.toString());
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							String resultString = CreateFile(savePath, response);
							if(resultString == "")
							{
								callBack.succeed();
							}else {
								callBack.failed(resultString);
							}
						}

					});
		} catch (Exception e) {
			callBack.failed(e.toString());
			e.printStackTrace();
		}
	}

	public String CreateFile(String savePath, byte[] response){
		try {
			File file = new File(savePath);
			
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileOutputStream oStream = new FileOutputStream(file);
			oStream.write(response);
			oStream.flush();
			oStream.close();
			Log.d("httpD", "保存到：" + savePath);
			return "";
			
		} catch (Exception e) {
			Log.e("httpD", e.getMessage());
			e.printStackTrace();
			
			return "保存文件失败："+ e.getMessage();
			
		}
	}

	
	public void upload(final String filePath, final UploadCallBack callBack) {
		try {
			File myFile = new File(filePath);
			if(!myFile.exists())
			{
				callBack.failed("找不到文件：" + filePath);
				return;
			}
			RequestParams params = new RequestParams();
			params.put("filename", myFile);

			HttpClientUtil.getClient().post(serveUrl, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] response, Throwable throwable) {
							Log.d("http",
									"fail:" + statusCode + throwable.toString());
							
							callBack.failed("http错误：" + statusCode + throwable.toString());
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							try {
								Log.d("http", "success:" + statusCode);
								if (response != null) {
									String resposeString = new String(response);
									Log.d("http", resposeString);
									JSONObject responseJo = new JSONObject(
											resposeString);

									int retcode = responseJo.getInt("retcode");
									String downloadUrl = responseJo
											.getString("url");
									String errmsg = responseJo
											.getString("errormsg");

									switch (retcode) {
									case 0:
										callBack.succeed(downloadUrl);
										break;
									default:
										callBack.failed(errmsg);
										break;
									}
								}
							} catch (Exception e) {
								callBack.failed(e.getMessage());
								e.printStackTrace();
							}

						}

					});

		} catch (FileNotFoundException e) {
			callBack.failed(e.getMessage());
			e.printStackTrace();
		}
	}

}
