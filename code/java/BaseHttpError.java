package com.bq.http;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by xiaob on 2018/3/7.
 */

class BaseHttpError implements IHttpError {

    private final String tag = getClass().toString();

    private final HttpExecutor httpExecutor;

    public BaseHttpError(HttpExecutor httpExecutor) {
        this.httpExecutor = httpExecutor;
    }

    @Override
    public void error(HttpCallbackContext context) {
        IOException e = context.getException();
        e.printStackTrace();
        final String text = "请求[" + context.url() + "]发生错误，错误消息" + e.getMessage();
        Log.e(tag, text);
        if (null != httpExecutor.activity) {
            httpExecutor.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(httpExecutor.activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
