package com.chan.retrofit3lib;

import android.text.TextUtils;

import com.chan.retrofit3lib.core.MethodHandler;
import com.chan.retrofit3lib.core.facotry.SimpleCallAdapterFactory;
import com.chan.retrofit3lib.core.interfaces.ICallAdapter;
import com.chan.retrofit3lib.core.interfaces.IConverter;
import com.chan.retrofit3lib.utils.Utils;
import com.squareup.okhttp.OkHttpClient;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chan on 16/6/3.
 */
public class Retrofit3 {

    private String m_baseUri;
    private OkHttpClient m_okHttpClient;
    private Map<Method, MethodHandler<?>> m_methodHandlerHashMap = new HashMap<>();
    private IConverter.IConverterFactory m_converterFactory;
    private ICallAdapter.CallAdapterFactory m_callAdapterFactory;

    public Retrofit3(String baseUri, OkHttpClient okHttpClient, IConverter.IConverterFactory converterFactory,
                     ICallAdapter.CallAdapterFactory callAdapterFactory) {
        m_baseUri = baseUri;
        m_okHttpClient = okHttpClient;
        m_converterFactory = converterFactory;
        m_callAdapterFactory = callAdapterFactory;
    }

    public String getBaseUri() {
        return m_baseUri;
    }

    public void setBaseUri(String baseUri) {
        m_baseUri = baseUri;
    }

    public OkHttpClient getOkHttpClient() {
        return m_okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        m_okHttpClient = okHttpClient;
    }

    public IConverter.IConverterFactory getConverterFactory() {
        return m_converterFactory;
    }

    public void setConverterFactory(IConverter.IConverterFactory converterFactory) {
        m_converterFactory = converterFactory;
    }

    public ICallAdapter.CallAdapterFactory getCallAdapterFactory() {
        return m_callAdapterFactory;
    }

    public ICallAdapter<?> getCallAdapter(Method method) {

        //获取方法所有的注解
        Annotation[] annotations = method.getAnnotations();

        //根据方法返回类型  注解 Retrofit3对象 找到合适的CallAdapter
        ICallAdapter<?> callAdapter = m_callAdapterFactory.get(method.getGenericReturnType(), annotations, this);

        if (callAdapter == null) {
            throw new IllegalStateException("can not get call adapter");
        }

        return callAdapter;
    }

    /**
     * 生成对应的api
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz) {
        Utils.checkNotNull(clazz, "clazz must not be null");

        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //这里是原版的一个简化
                //原版这里有个很精妙的类 Platform 有兴趣的读者可以自行查阅

                //如果是toString这类函数 则调用当前匿名类自己的Object的方法
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }

                //
                return getMethodHandler(method).invoke(args);
            }
        });
    }

    public IConverter<?> responseConverter(Type responseType, Annotation[] annotations) {
        return m_converterFactory.create(responseType, annotations);
    }

    /**
     * 获得对应的Method handler
     * @param method
     * @return
     */
    private MethodHandler<?> getMethodHandler(Method method) {

        MethodHandler<?> result = null;

        synchronized (m_methodHandlerHashMap) {

            result = m_methodHandlerHashMap.get(method);

            if (result == null) {

                result = MethodHandler.create(this, method);
                m_methodHandlerHashMap.put(method, result);
            }
        }

        return result;
    }

    public static class Builder {
        private String m_baseUri;
        private OkHttpClient m_okHttpClient;
        private IConverter.IConverterFactory m_converterFactory;
        private ICallAdapter.CallAdapterFactory m_callAdapterFactory;

        public Builder baseUri(String baseUri) {
            m_baseUri = baseUri;
            return this;
        }

        public Retrofit3 build() {

            if (TextUtils.isEmpty(m_baseUri)) {
                throw new IllegalStateException("base uri must not be null");
            }

            Utils.checkNotNull(m_converterFactory, "convert adapter factory is null");

            if (m_okHttpClient == null) {
                m_okHttpClient = new OkHttpClient();
            }

            if (m_callAdapterFactory == null) {
                m_callAdapterFactory = SimpleCallAdapterFactory.create();
            }

            return new Retrofit3(m_baseUri, m_okHttpClient, m_converterFactory, m_callAdapterFactory);
        }

        public Builder callAdapterFactory(ICallAdapter.CallAdapterFactory callAdapterFactory) {
            m_callAdapterFactory = callAdapterFactory;
            return this;
        }

        public Builder converterFactory(IConverter.IConverterFactory converterFactory) {
            m_converterFactory = converterFactory;
            return this;
        }
    }
}
