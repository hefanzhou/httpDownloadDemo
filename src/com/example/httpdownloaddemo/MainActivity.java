package com.example.httpdownloaddemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.util.http.DownloadCallBack;
import com.util.http.FileTransfer;
import com.util.http.HttpClientUtil;
import com.util.http.UploadCallBack;

public class MainActivity extends Activity implements OnClickListener {

	private String urlString = "http://221.228.210.7:2080/voice/api/upload";
	private String downloadPath = "/storage/emulated/0/2myfile";
	private String filename = "test.m4a";
	private String downloadUrl = "";

	String fileContext = "";
	void intiFilContext()
	{
		for(int i = 0; i < 1000; i++)
		{
			fileContext += "qwerwerwqerqwerwer";
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Button updeteBtn = (Button) findViewById(R.id.updateBtn);
		updeteBtn.setOnClickListener(this);

		Button downLoadBtn = (Button) findViewById(R.id.downloadBtn);
		downLoadBtn.setOnClickListener(this);

		Button creatFileBtn = (Button) findViewById(R.id.createFileBtn);
		creatFileBtn.setOnClickListener(this);
		
		FileTransfer.SetServeUrl(urlString);
	}

	//http://221.228.210.7:2080/voice/api/download?id=FujvmNKaHwdQHaZPQTfH3M
	@Override
	public void onClick(View v) {

		int id = v.getId();

		switch (id) {
		case R.id.updateBtn:
			FileTransfer.get().upload(downloadPath +"/"+ filename, new UploadCallBack() {
				
				@Override
				public void succeed(String _downloadUrl) {
					downloadUrl = _downloadUrl;
					Log.d("http", "success url:" + downloadUrl);
				}
				
				@Override
				public void failed(String errmsg) {
					Log.d("http", "file:" + errmsg);
				}
			});
			break;
		case R.id.downloadBtn:
			
			FileTransfer.get().download("http://221.228.210.7:2080/voice/api/download?id=FujvmNKaHwdQHaZPQTfH3M", downloadPath+"/"+"1formSever.txt", new DownloadCallBack() {
				
				@Override
				public void succeed() {
					// TODO Auto-generated method stub
					Log.d("http", "success");
				}
				
				@Override
				public void failed(String errmsg) {
					Log.d("http", errmsg);
				}
			});
			
			
			break;

		case R.id.createFileBtn:
			
			intiFilContext();
			FileTransfer.SetServeUrl(urlString);
			FileTransfer.get().CreateFile(downloadPath + "/"+"1formSever.txt", fileContext.getBytes());
		default:
			break;
		}
	}


	

}
