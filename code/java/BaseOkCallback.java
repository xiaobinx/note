package com.bq.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaob on 2018/3/7.
 */

public class BaseOkCallback implements Callback {

    private final IHttpOk iHttpOk;

    private final IHttpError iHttpError;

    private final IHttpFail iHttpFail;

    public BaseOkCallback(IHttpOk iHttpOk, IHttpFail iHttpFail, IHttpError iHttpError) {
        this.iHttpOk = iHttpOk;
        this.iHttpFail = iHttpFail;
        this.iHttpError = iHttpError;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        HttpCallbackContext context = new HttpCallbackContext(call, e);
        iHttpError.error(context);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        int code = response.code();
        HttpCallbackContext context = new HttpCallbackContext(call, response);
        if (200 == code) {
            iHttpOk.ok(context);
        } else {
            iHttpFail.fail(context);
        }
    }
}
