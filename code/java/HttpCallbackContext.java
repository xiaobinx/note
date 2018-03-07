package com.bq.http;

import com.bq.http.exception.IORuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by xiaob on 2018/3/7.
 */

public class HttpCallbackContext {
    public final Response response;
    public final Call call;
    public final IOException e;

    public HttpCallbackContext(Call call, Response response) {
        this.response = response;
        this.call = call;
        this.e = null;
    }

    public HttpCallbackContext(Call call, IOException e) {
        this.response = null;
        this.call = call;
        this.e = e;
    }

    public IOException getException() {
        return e;
    }

    public int code() {
        return response.code();
    }

    public String string() {
        try {
            return response.body().string();
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }

    public InputStream getInputStream() {
        return response.body().byteStream();
    }

    public String url() {
        return call.request().url().toString();
    }

}
