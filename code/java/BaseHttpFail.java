package com.bq.http;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by xiaob on 2018/3/7.
 */

class BaseHttpFail implements IHttpFail {

    private final String tag = getClass().toString();

    private final HttpExecutor httpExecutor;

    public BaseHttpFail(HttpExecutor httpExecutor) {
        this.httpExecutor = httpExecutor;
    }

    @Override
    public void fail(HttpCallbackContext context) {
        int code = context.code();
        String url = context.url();
        final String text = "请求[" + url + "]失败，HTTP状态码：" + code;
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
