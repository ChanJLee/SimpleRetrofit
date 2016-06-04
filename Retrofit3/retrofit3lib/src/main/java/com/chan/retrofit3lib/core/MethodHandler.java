package com.chan.retrofit3lib.core;

import com.chan.retrofit3lib.Retrofit3;
import com.chan.retrofit3lib.core.interfaces.ICallAdapter;
import com.chan.retrofit3lib.core.interfaces.IConverter;
import com.chan.retrofit3lib.core.impl.OkHttpCall;
import com.chan.retrofit3lib.core.facotry.RequestFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public class MethodHandler<T> {

    private Retrofit3 m_retrofit3;
    private RequestFactory m_requestFactory;
    private ICallAdapter<T> m_callAdapter;
    private IConverter<T> m_converter;

    public MethodHandler(Retrofit3 retrofit3, RequestFactory requestFactory, ICallAdapter<T> callAdapter, IConverter converter) {
        m_retrofit3 = retrofit3;
        m_requestFactory = requestFactory;
        m_callAdapter = callAdapter;
        m_converter = converter;
    }

    /**
     * 方法调用
     * @param args 参数
     * @return 获取到服务器返回值后 返回的java对象
     * @throws IOException
     */
    public Object invoke(Object... args) throws IOException {

        //到这里可以看到callAdapter用于修饰call
        return m_callAdapter.adapt(new OkHttpCall<>(m_retrofit3, m_requestFactory, m_converter, args));
    }

    public static MethodHandler<?> create(Retrofit3 retrofit3, Method method) {

        //用于适配返回值 把Call<T>类型的返回值换成别的类型 比如 T类型  或者  Observable<T>类型
        ICallAdapter<?> callAdapter = retrofit3.getCallAdapter(method);

        //获取Call<T>的模板参数类型T 这个T 将被Converter用于生成对应的实体类对象
        Type responseType = callAdapter.getResponseType();

        //用于把服务器对象生成对应的java实体类对象 比如根据json字符串 生成java实体类对象
        IConverter<?> iConverter = retrofit3.responseConverter(responseType, method.getAnnotations());

        //用于根据每次方法调用 生成对应的okhttp request
        RequestFactory requestFactory = RequestFactory.parse(method, retrofit3);

        //返回它
        return new MethodHandler<>(retrofit3, requestFactory, callAdapter, iConverter);
    }
}
