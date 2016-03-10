package com.ziben365.ocapp.util.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

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
public class JsonStringRequest extends Request<String> {

    private Response.Listener<String> mListener;

    public JsonStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    public JsonStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        InputStream is = new ByteArrayInputStream(response.data);
        MessagePack messagePack = new MessagePack();
        Unpacker messagePackUnpacker = messagePack.createUnpacker(is);
        Object object = null;
        try {
            object = ObjectTemplate.getInstance().read(messagePackUnpacker, Object.class, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success(object.toString(), HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
