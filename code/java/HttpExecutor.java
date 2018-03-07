package com.bq.http;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by xiaob on 2018/3/6.
 */

public class HttpExecutor {

    private final static String METHOD_GET = "GET";

    private final static String METHOD_POST = "POST";

    private final String url;

    private final Request.Builder requestBuilder;

    private Gson gson;

    private OkHttpClient client = HttpClientTool.getNoCacheClient();

    /**
     * 表单请求构造器
     */
    private FormBody.Builder formBodyBuilder;

    /**
     * post请求时使用
     */
    private RequestBody body;

    /**
     * 请求方式，目前没什么用姑且留着
     */
    private String method;

    /**
     * 当前context一般与activity为同一个对象
     */
    private Context context;

    /**
     * 当前activity一般与context为同一个对象
     */
    Activity activity;

    /**
     * Okhttpclient请求回调函数。若这个参数为空，执行exec方法时默认构造com.bq.http.BaseOkCallback。
     */
    private Callback callback;
    /**
     * http成功时(code=200)的回掉函数，callback参数为空时有效
     */
    private IHttpOk iHttpOk;
    /**
     * http失败时(code!=200)的回掉函数，callback参数为空时有效
     */
    private IHttpFail iHttpFail;
    /**
     * 请求因异常失败时的回掉函数，callback参数为空时有效
     */
    private IHttpError iHttpError;

    /**
     * 创建一个HTTP请求执行器
     *
     * @param url
     */
    private HttpExecutor(String url) {
        this.url = url;
        this.requestBuilder = new Request.Builder();

        iHttpFail = new BaseHttpFail(this);
        iHttpError = new BaseHttpError(this);
        iHttpOk = new BaseHttpOk();
    }

    /**
     * 创建GET请求时调用
     *
     * @return 当前HttpExecutor实例
     */
    private HttpExecutor get() {
        this.method = METHOD_GET;
        requestBuilder.get().url(url);
        return this;
    }

    /**
     * 创建POST请求时调用
     *
     * @return 当前HttpExecutor实例
     */
    private HttpExecutor post() {
        this.method = METHOD_POST;
        requestBuilder.get().url(url);
        return this;
    }

    /**
     * 设置发送文本
     *
     * @param text 请求文本
     * @return 当前HttpExecutor实例
     */
    public HttpExecutor text(String text) {
        this.body = RequestBody.create(MediaType.parse("text/plain"), text);
        return this;
    }

    /**
     * 设置发送文本
     *
     * @param json 请求json文本
     * @return 当前HttpExecutor实例
     */
    public HttpExecutor json(String json) {
        this.body = RequestBody.create(MediaType.parse("application/json"), json);
        return this;
    }

    /**
     * 设置发送文本
     *
     * @param obj 请求对象，由成员变量gson转换成json文本
     * @return 当前HttpExecutor实例
     */
    public HttpExecutor json(Object obj) {
        if (null == gson) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        }
        String json = gson.toJson(obj);
        this.body = RequestBody.create(MediaType.parse("application/json"), json);
        return this;
    }

    /**
     * 添加表单参数
     *
     * @param name  参数名称
     * @param value 参数值
     * @return 当前HttpExecutor实例
     */
    public HttpExecutor addFormParam(String name, String value) {
        if (null == formBodyBuilder) {
            formBodyBuilder = new FormBody.Builder();
        }
        formBodyBuilder.add(name, value);
        return this;
    }

    /**
     * 开始异步发起HTTP请求
     */
    public void exec() {
        if (null != body) {
            requestBuilder.post(body);
        } else if (null != formBodyBuilder) {
            body = formBodyBuilder.build();
            requestBuilder.post(body);
        }// else post同get
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        if (null == callback) {
            callback = new BaseOkCallback(iHttpOk, iHttpFail, iHttpError);
        }
        call.enqueue(callback);
    }

    /**
     * 设置当前的context，将切换使用有缓存的okhttpclient，并尝试设置当前activiy
     *
     * @param context 当前context
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor context(Context context) {
        this.context = context;
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        client = HttpClientTool.getCacheClient(context);
        return this;
    }

    /**
     * 设置当前的activty，同时设置context，将使用有缓存的okhttpclient
     *
     * @param activity 当前Activity
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor activity(Activity activity) {
        this.activity = activity;
        this.context = activity;
        client = HttpClientTool.getCacheClient(context);
        return this;
    }

    /**
     * 自定义OKhttp的请求回掉函数，使得iHttpOk，iHttpFail，iHttpError;三个参数不再起作用
     *
     * @param callback OKhttpclient请求回掉函数
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置http成功时的回掉函数，callback参数为空时有效
     *
     * @param iHttpOk http成功时的回掉函数
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor ok(IHttpOk iHttpOk) {
        this.iHttpOk = iHttpOk;
        return this;
    }

    /**
     * http失败时(code!=200)的回掉函数，callback参数为空时有效
     *
     * @param iHttpFail http失败时(code!=200)的回掉函数
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor fail(IHttpFail iHttpFail) {
        this.iHttpFail = iHttpFail;
        return this;
    }

    /**
     * 请求因异常失败时的回掉函数，callback参数为空时有效
     *
     * @param iHttpError 请求因异常失败时的回掉函数
     * @return 返回当前HttpExecutor实例
     */
    public HttpExecutor error(IHttpError iHttpError) {
        this.iHttpError = iHttpError;
        return this;
    }

    /**
     * 创建一个HttpExecutor
     *
     * @param url 请求url
     * @return 返回创建的HttpExecutor实例
     */
    public final static HttpExecutor get(String url) {
        return new HttpExecutor(url).get();
    }

    /**
     * 创建一个HttpExecutor
     *
     * @param context 当前使用的context
     * @param url     请求url
     * @return 返回创建的HttpExecutor实例
     */
    public final static HttpExecutor get(Context context, String url) {
        return new HttpExecutor(url).get().context(context);
    }

    /**
     * 创建一个HttpExecutor
     *
     * @param url 请求url
     * @return 返回创建的HttpExecutor实例
     */
    public final static HttpExecutor post(String url) {
        return new HttpExecutor(url).post();
    }

    /**
     * 创建一个HttpExecutor
     *
     * @param context 当前使用的context
     * @param url     请求url
     * @return 返回创建的HttpExecutor实例
     */
    public final static HttpExecutor post(Context context, String url) {
        return new HttpExecutor(url).post().context(context);
    }

}
