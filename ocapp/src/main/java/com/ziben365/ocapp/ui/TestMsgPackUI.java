package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.request.ObjectTemplate;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.msgpack.MessagePack;
import org.msgpack.unpacker.Unpacker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/15.
 * email  1956766863@qq.com
 */
public class TestMsgPackUI extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.btn)
    Button btn;
    @InjectView(R.id.tv)
    TextView tv;

    private static final String url = "http://test.api.ziben365.com/test2/choice";
//    private static final String url = "http://m.ziben365.com/about/test1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_window);
        ButterKnife.inject(this);

        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                applyData();
//            }
//        }).start();

        VolleyRequest.volleyStream(url, "", new VolleyInterface() {
            @Override
            public void onSuccess(Object o) {
                InputStream is = (InputStream) o;
                MessagePack msgpack = new MessagePack();
                Unpacker unpacker = msgpack.createUnpacker(is);
                L.i("--------unpacker---------" + unpacker.getReadByteCount());
                try {
                    Object testData = ObjectTemplate.getInstance().read(unpacker, Object.class, true);
                    L.i("-----55555555555-----" + testData.toString());
                    tv.setText(testData.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

//        VolleyRequest.volleyRequestPost(url, "", new VolleyInterface() {
//            @Override
//            public void onSuccess(Object o) {
//                L.i(o.toString());
////                tv.setText(o.toString());
//                InputStream is = new ByteArrayInputStream();
//                MessagePack msgpack = new MessagePack();
//                Unpacker unpacker = msgpack.createUnpacker(is);
//                L.i("--------unpacker---------" + unpacker.getReadByteCount());
//                try {
//                    Object testData = ObjectTemplate.getInstance().read(unpacker, Object.class, true);
//                    L.i("-----55555555555-----" + testData.toString());
//                    tv.setText(testData.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        });

    }

    private void applyData() {
        HttpClient mHttpClient = new DefaultHttpClient();
        mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
        mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-Type", "application/x-msgpack");

        try {
//			httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
            HttpResponse response = mHttpClient.execute(httpPost);
            if (404 != response.getStatusLine().getStatusCode()) {
                HttpEntity messageEntity = response.getEntity();
                MessagePack msgpack = new MessagePack();
                InputStream is = messageEntity.getContent();
                Unpacker unpacker = msgpack.createUnpacker(is);
                Object testData = ObjectTemplate.getInstance().read(unpacker, Object.class, true);
                L.i("-----55555555555-----" + testData.toString());
                Message msg = Message.obtain(handler, 0, testData);
                msg.sendToTarget();

            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void IntoString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }
            Message msg = Message.obtain(handler, 0, sb.toString());
            msg.sendToTarget();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();
            tv.setText(data);
        }
    };
}
