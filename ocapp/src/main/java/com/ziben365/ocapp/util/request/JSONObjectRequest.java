package com.ziben365.ocapp.util.request;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ziben365.ocapp.util.GsonUtil;

import org.json.JSONObject;
import org.msgpack.MessagePack;
import org.msgpack.unpacker.Unpacker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/15.
 * email  1956766863@qq.com
 */
public class JSONObjectRequest extends Request<JSONObject> {
    private Response.Listener<JSONObject> mListener;
    public JSONObjectRequest(String url, Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {

        this(Method.GET,url, listener,errorListener);
    }

    public JSONObjectRequest(int method, String url, Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        InputStream is = new ByteArrayInputStream(response.data);
        MessagePack messagePack = new MessagePack();
        Unpacker messagePackUnpacker = messagePack.createUnpacker(is);
        Object object = null;
        try {
            object = ObjectTemplate.getInstance().read(messagePackUnpacker, Object.class, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = object.toString();
        if (!TextUtils.isEmpty(result)){
            JSONObject jsonObject = GsonUtil.pareJSONObject(result);
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        }
        return null;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
