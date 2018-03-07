package com.bq.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by xiaob on 2018/3/7.
 */

public class BqCookieJar implements CookieJar {

//    private final String tag = BqCookieJar.class.toString();

    private final Map<String, Map<String, Cookie>> cookieStore = new HashMap<String, Map<String, Cookie>>();// 以domain,name为键值存储cookie

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> newCookies) {
//        Log.d(tag, "set------------------------------------------->");
//        for (Cookie cookie : newCookies) {
//            Log.d(tag, "set from response " + LogStr(url, cookie));
//        }

        for (Cookie newCookie : newCookies) {
            String domain = newCookie.domain();
            if (cookieStore.containsKey(domain)) {
                Map<String, Cookie> cookies = cookieStore.get(domain);
//                Cookie cookie =
                cookies.put(newCookie.name(), newCookie);// 暂时不允许同一域下相同的cookie值
//                if (null != cookie) {
//                    Log.d(tag, "remove->" + LogStr(url, cookie));
//                }
//                Log.d(tag, "add->" + LogStr(url, newCookie));
            } else {
                Map<String, Cookie> cookies = new HashMap<String, Cookie>();
                cookies.put(newCookie.name(), newCookie);
//                Log.d(tag, "add->" + LogStr(url, newCookie));
                cookieStore.put(domain, cookies);
            }
        }
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        String host = url.host();
        Set<Map.Entry<String, Map<String, Cookie>>> set = cookieStore.entrySet();
        List<Cookie> cookies = new LinkedList<>();
        List<Cookie> rmCookies = new LinkedList<>();//需要移除的cookie
        long time = System.currentTimeMillis();
        for (Map.Entry<String, Map<String, Cookie>> entry : set) {
            String domain = entry.getKey();
            if (host.endsWith(domain)) {// 与请求url匹配的所有域
                Collection<Cookie> domainCookies = entry.getValue().values();
                for (Cookie cookie : domainCookies) {
                    if (cookie.expiresAt() < time) {
                        rmCookies.add(cookie);
                    } else if (cookie.matches(url)) {// 匹配域下，再用matches匹配合适的cookie
                        cookies.add(cookie);
                    }
                }
            }
        }

        if (rmCookies.size() > 0) {// 清理过期cookie
            for (Cookie cookie : rmCookies) {
                cookieStore.get(cookie.domain()).remove(cookie.name());
            }
        }

//        Log.d(tag, "return to response ------------------------------------------->");
//        for (Cookie cookie : cookies) {
//            Log.d(tag, "return->" + LogStr(url, cookie));
//        }
//
//        for (Map.Entry<String, Map<String, Cookie>> entry : set) {
//            Collection<Cookie> list = entry.getValue().values();
//            for (Cookie cookie : list) {
//                Log.d(tag, "have->" + LogStr(url, cookie));
//            }
//        }

        return cookies;
    }

    private String logStr(HttpUrl url, Cookie cookie) {
        return "requet:[url=" + url + ", host=" + url.host() + "]" +
                ",do cookis[" +
                "domain=" + cookie.domain() +
                ", name=" + cookie.name() +
                ", value=" + cookie.value() +
                ", path=" + cookie.path() +
                ", hostOnly=" + cookie.hostOnly() +
                ", httpOnly=" + cookie.httpOnly() +
                ", secure=" + cookie.secure() +
                ", expiresAt=" + cookie.expiresAt() +
                "]";
    }
}
