package com.util.http;

public interface UploadCallBack {

	  public abstract  void succeed(String downloadUrl);

	  public  abstract void failed(String errmsg);
}
