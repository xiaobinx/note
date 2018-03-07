package com.bq.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by xiaob on 2018/3/6.
 */

public class HttpClientTool {

    private final static String tag = HttpClientTool.class.toString();

    private HttpClientTool() {
    }

    private static OkHttpClient NO_CACHE_CLIENT;

    private static OkHttpClient CACHE_CLIENT;

    private final static OkHttpClient.Builder createCommonClientBuilder() {
        return new OkHttpClient.Builder();
    }

    private synchronized static OkHttpClient initNoCacheClient() {
        if (null == NO_CACHE_CLIENT) {
            NO_CACHE_CLIENT = createCommonClientBuilder().build();
        }
        return NO_CACHE_CLIENT;
    }

    private synchronized static OkHttpClient initCacheClient(Context context) {
        if (null == CACHE_CLIENT) {
            File dir = new File(context.getCacheDir(), "okhttpcache");
            if (!dir.exists()) {
                dir.mkdir();
            }
            Cache cache = new Cache(dir, 1024 * 1024 * 20);
            CACHE_CLIENT = createCommonClientBuilder()
                    .cookieJar(new BqCookieJar())
                    .cache(cache)
                    .build();
        }
        return CACHE_CLIENT;
    }


    public final static OkHttpClient newClient() {
        return createCommonClientBuilder().build();
    }

    public final static OkHttpClient getNoCacheClient() {
        OkHttpClient client = NO_CACHE_CLIENT;
        if (null == NO_CACHE_CLIENT) {
            NO_CACHE_CLIENT = initNoCacheClient();
        }
        return NO_CACHE_CLIENT;
    }

    public final static OkHttpClient getCacheClient(Context context) {
        OkHttpClient client = CACHE_CLIENT;
        if (null == CACHE_CLIENT) {
            CACHE_CLIENT = initCacheClient(context);
        }
        return CACHE_CLIENT;
    }
}
