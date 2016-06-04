package com.chan.retrofit3lib.core.impl;

import com.chan.retrofit3lib.Retrofit3;
import com.chan.retrofit3lib.core.facotry.RequestFactory;
import com.chan.retrofit3lib.core.interfaces.Call;
import com.chan.retrofit3lib.core.interfaces.IConverter;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by chan on 16/6/4.
 */
public class OkHttpCall<T> implements Call<T> {

    private IConverter<T> m_converter;
    private Object[] m_args;
    private Retrofit3 m_retrofit3;
    private RequestFactory m_requestFactory;

    public OkHttpCall(Retrofit3 retrofit3, RequestFactory requestFactory, IConverter<T> iConverter, Object... args) {
        m_args = args;
        m_retrofit3 = retrofit3;
        m_converter = iConverter;
        m_requestFactory = requestFactory;
    }

    /**
     * 这里对应我们示例之中的同步网络请求
     * @return
     * @throws IOException
     */
    public T execute() throws IOException {

        //获取OkHttp的call对象
        com.squareup.okhttp.Call call = getRawCall();

        //解析出服务器的返回值
        return parseResponse(call.execute());
    }

    private com.squareup.okhttp.Call getRawCall() {
        //通过request factory创建对应的request
        //然后返回对应的OKHttp call
        return m_retrofit3.getOkHttpClient().newCall(m_requestFactory.newRequest(m_args));
    }

    /**
     * 解析出服务器返回的对象
     * @param response okhttp的response
     * @return
     * @throws IOException
     */
    private T parseResponse(Response response) throws IOException {

        //通过convert将response转换成java实体类对象
        return m_converter.convert(response);
    }
}
