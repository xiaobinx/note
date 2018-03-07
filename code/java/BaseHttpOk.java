package com.bq.http;

/**
 * Created by xiaob on 2018/3/7.
 */

class BaseHttpOk implements IHttpOk {
    @Override
    public void ok(HttpCallbackContext httpCallbackContext) {
        throw new RuntimeException("没有设置IHttpOk回掉");
    }
}
