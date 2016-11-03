package com.everyoo.volleydamon;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by abc on 2016/11/3.
 */
public class ByteArrayRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Object mPostBody = null;
    private HttpEntity httpEntity = null;
    HashMap<String, String> headers;

    public ByteArrayRequest(int method, String url, Object mPostBody, HashMap<String, String> headers, Response.Listener<byte[]> mListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mPostBody = mPostBody;
        this.mListener = mListener;
        if (headers != null) {
            this.headers = headers;
        } else {
            this.headers = new HashMap<String, String>();
        }
    }


    @Override
    public HashMap<String, String> getHeaders() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String) {
            //表单形式
            headers.put("Accept", "application/x-www-form-urlencoded");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        } else {
            //json形式
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
        }
        return headers;
    }

    @Override
    public String getBodyContentType() {
        if (httpEntity != null) {
            return httpEntity.getContentType().getValue();
        }
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null) {
            String postString = mPostBody.toString();
            if (postString.length() != 0) {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        if (this.httpEntity != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();
    }


    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(networkResponse.data, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(byte[] bytes) {
        this.mListener.onResponse(bytes);
    }
}
