package com.inksci.face_devel2_inker;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;


import java.io.IOException;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
 
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.client.params.HttpClientParams;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.params.BasicHttpParams;  
import org.apache.http.params.HttpConnectionParams;  
import org.apache.http.params.HttpParams;  
import org.apache.http.params.HttpProtocolParams;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
 
import android.app.Activity;  
import android.os.Bundle;  
import android.util.Log;  
import android.widget.EditText;  
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
   private HttpParams httpParams;  
    
   private HttpClient httpClient; 
   
   private EditText editText;
 
   
   private static final int MSG_OK = 1;
   private static final int EXCEPTION = 2;
   //1.�����߳�����������Ϣ������ handler
   private Handler handler = new Handler(){
       //������Ϣ��
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what) {
               case MSG_OK:
                   //3.������Ϣ ���������߳�
            	   String txt_data=(String)msg.obj;
            	   editText.setText(txt_data);
            	   // String -> JSON -> display it
			JSONObject jsonObject;
			try {
				editText.setText("...");
				jsonObject = new JSONObject(txt_data);
				JSONArray jsonArray = jsonObject.getJSONArray("faces"); 
				JSONObject jsonObject2 = (JSONObject)jsonArray.opt(0); 
				String face_token=jsonObject2.getString("face_token");
				JSONObject attributes = jsonObject2.getJSONObject("attributes");
				String gender = attributes.getJSONObject("gender").getString("value");
				String age = attributes.getJSONObject("age").getString("value");
				editText.setText("gender: "+gender+", age: "+age+", face_token: "+face_token);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                   
            	      	   
            	   
                   System.out.println("76799679");
                   break;
               case EXCEPTION:
                   Toast.makeText(MainActivity.this, "�����쳣������ʧ��", Toast.LENGTH_SHORT).show();
                   break;
           }

           super.handleMessage(msg);
       }
   };
   
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        editText = (EditText) this.findViewById(R.id.editText1);  
        
        
        
        new Thread(){
            public void run() {
                http_use();
            }
        }.start();
        
        editText.setText("123...");
        
    }

	private void http_use() {
		/*
		 * =====================================
		curl -X POST "https://api.megvii.com/facepp/v3/detect" \
		-F "api_key=Iwe59oTUN5GFG39IPUQVbOJ7iCA_hmaN" \
		-F "api_secret=EzzLLQB8wFvFObPEVRjYb0S-_UnUZf2f" \
		-F "image_url=http://inksci.com/w/tmp/sg-67698.jpg" \
		-F "return_landmark=1" \
		-F "return_attributes=gender,age"*/		
				
				
				
				
				try{
				// TODO Auto-generated method stub
		        List<NameValuePair> params = new ArrayList<NameValuePair>();  
		        params.add(new BasicNameValuePair("api_key", "Iwe59oTUN5GFG39IPUQVbOJ7iCA_hmaN"));  
		        params.add(new BasicNameValuePair("api_secret", "EzzLLQB8wFvFObPEVRjYb0S-_UnUZf2f"));  
		        params.add(new BasicNameValuePair("image_url", "http://inksci.com/w/tmp/sg-67698.jpg"));  
		        params.add(new BasicNameValuePair("return_landmark", "1"));  
		        params.add(new BasicNameValuePair("return_attributes", "gender,age"));   
  
        Map params2 = new HashMap();  
  
        params2.put("hl", "zh-CN");  
  
        params2.put("source", "hp");  
  
        params2.put("q", "haha");  
  
        params2.put("aq", "f");  
  
        params2.put("aqi", "g10");  
  
        params2.put("aql", "");  
  
        params2.put("oq", "");  
  
        String url2 = "http://www.google.cn/search";  
  
        String url = "https://api.megvii.com/facepp/v3/detect"; //"http://www.inksci.com/app/th-week.php";  
  
        getHttpClient();  
  
        //editText.setText(doPost(url, params));  
        //����ui ����д�����߳�
        Message msg = new Message();//������Ϣ
        msg.what = MSG_OK;
        msg.obj = doPost(url, params);//��������
        handler.sendMessage(msg);//��handler�����Ƿ�������
  
        // editText.setText(doGet(url2, params2)); 		
	} catch (Exception e) {
            e.printStackTrace();
            //��˾����д�����߳�
            //Toast.makeText(this, "�����쳣������ʧ��", 0).show();
            Message msg = new Message();
            msg.what = EXCEPTION;
            handler.sendMessage(msg);
    }
		
	};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    public String doGet(String url, Map params) {  
    	  
        /* ����HTTPGet���� */  
  
        String paramStr = "";  
  
        Iterator iter = params.entrySet().iterator();  
        while (iter.hasNext()) {  
            Map.Entry entry = (Map.Entry) iter.next();  
            Object key = entry.getKey();  
            Object val = entry.getValue();  
            paramStr += paramStr = "&" + key + "=" + val;  
        }  
  
        if (!paramStr.equals("")) {  
            paramStr = paramStr.replaceFirst("&", "?");  
            url += paramStr;  
        }  
        HttpGet httpRequest = new HttpGet(url);  
  
        String strResult = "doGetError";  
  
        try {  
  
            /* �������󲢵ȴ���Ӧ */  
            HttpResponse httpResponse = httpClient.execute(httpRequest);  
            /* ��״̬��Ϊ200 ok */  
            if (httpResponse.getStatusLine().getStatusCode() == 200) {  
                /* ���������� */  
                strResult = EntityUtils.toString(httpResponse.getEntity());  
  
            } else {  
                strResult = "Error Response: "  
                        + httpResponse.getStatusLine().toString();  
            }  
        } catch (ClientProtocolException e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        } catch (IOException e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        } catch (Exception e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        }  
  
        Log.v("strResult", strResult);  
  
        return strResult;  
    }  
  
    public String doPost(String url, List<NameValuePair> params) {  
  
        /* ����HTTPPost���� */  
        HttpPost httpRequest = new HttpPost(url);  
  
        String strResult = "doPostError";  
  
        try {  
            /* ������������������� */  
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));  
            /* �������󲢵ȴ���Ӧ */  
            HttpResponse httpResponse = httpClient.execute(httpRequest);  
            /* ��״̬��Ϊ200 ok */  
            if (httpResponse.getStatusLine().getStatusCode() == 200) {  
                /* ���������� */  
                strResult = EntityUtils.toString(httpResponse.getEntity());  
  
            } else {  
                strResult = "Error Response: "  
                        + httpResponse.getStatusLine().toString();  
            }  
        } catch (ClientProtocolException e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        } catch (IOException e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        } catch (Exception e) {  
            strResult = e.getMessage().toString();  
            e.printStackTrace();  
        }  
  
        Log.v("strResult", strResult);  
  
        return strResult;  
    }  
  
    public HttpClient getHttpClient() {  
  
        // ���� HttpParams ���������� HTTP ��������һ���ֲ��Ǳ���ģ�  
  
        this.httpParams = new BasicHttpParams();  
  
        // �������ӳ�ʱ�� Socket ��ʱ���Լ� Socket �����С  
  
        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);  
  
        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);  
  
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);  
  
        // �����ض���ȱʡΪ true  
  
        HttpClientParams.setRedirecting(httpParams, true);  
  
        // ���� user agent  
  
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";  
        HttpProtocolParams.setUserAgent(httpParams, userAgent);  
  
        // ����һ�� HttpClient ʵ��  
  
        // ע�� HttpClient httpClient = new HttpClient(); ��Commons HttpClient  
  
        // �е��÷����� Android 1.5 ��������Ҫʹ�� Apache ��ȱʡʵ�� DefaultHttpClient  
  
        httpClient = new DefaultHttpClient(httpParams);  
  
        return httpClient;  
    }  
    
    
    
}
