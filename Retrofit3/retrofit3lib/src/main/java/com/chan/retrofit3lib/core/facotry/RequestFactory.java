package com.chan.retrofit3lib.core.facotry;

import android.text.TextUtils;

import com.chan.retrofit3lib.Retrofit3;
import com.chan.retrofit3lib.annotation.Get;
import com.chan.retrofit3lib.annotation.Path;
import com.chan.retrofit3lib.annotation.Query;
import com.squareup.okhttp.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by chan on 16/6/3.
 */
public class RequestFactory {

    private Retrofit3 m_retrofit3;
    private Method m_method;

    private Annotation m_httpMethod;
    private String m_uri;

    /**
     * method对应的是接口中的方法
     * @param retrofit3
     * @param method
     */
    private RequestFactory(Retrofit3 retrofit3, Method method) {
        m_retrofit3 = retrofit3;
        m_method = method;

        //获得base uri
        m_uri = m_retrofit3.getBaseUri();

        //获得方法中的注解
        Annotation[] annotations = method.getAnnotations();

        for (int i = 0; annotations != null && i < annotations.length; ++i) {
            if(annotations[i] instanceof Get) {
                m_httpMethod = annotations[i];
            }
            //下面可以是post  put delete
        }
    }

    /**
     * 创建一个ok http 的 request对象
     * @param args
     * @return
     */
    public Request newRequest(Object... args) {

        //扫描method中的参数注解
        Annotation[][] annotations = m_method.getParameterAnnotations();

        String uri = m_uri;

        //如果是有注解的
        if (annotations != null && annotations.length != 0) {
            String path = "";

            //获得Get中的值 它将被添加到base uri后
            if (m_httpMethod instanceof Get) {
                path = ((Get) m_httpMethod).value();
            }

            //一次替换path中的字符串  Query注解对应于在字符串后面添加 诸如 ?A=B&C=D的字符串
            //Path 注解对应于 把诸如 /a/{b} 替换成 /a/b这样的代码
            boolean hasAppendQuestion = false;
            for (int i = 0; i < annotations.length; ++i) {
                Annotation[] array = annotations[i];
                for (int j = 0; j < array.length; ++j) {
                    if (array[j] instanceof Path) {
                        Path p = (Path) array[j];
                        if (TextUtils.isEmpty(p.value())) {
                            throw new IllegalStateException("null path");
                        }

                        String target = "\\{" + p.value() + "\\}";
                        path = path.replaceAll(target, (String) args[i]);
                    } else if (array[j] instanceof Query) {
                        Query query = (Query) array[j];
                        if (TextUtils.isEmpty(query.value())) {
                            throw new IllegalStateException("null query");
                        }

                        if (hasAppendQuestion) {
                            path += "&";
                        } else {
                            path += "?";
                            hasAppendQuestion = true;
                        }
                        path += query.value();
                        path += "=";
                        path += args[i];
                    }
                }
            }

            if (!TextUtils.isEmpty(path)) {
                uri += path;
            }
        }

        //创建一个okhttp request对象
        Request.Builder builder = new Request.Builder();
        builder.url(uri);

        if (m_httpMethod instanceof Get) {
            builder.get();
        }

        return builder.build();
    }

    public static RequestFactory parse(Method method, Retrofit3 retrofit) {
        return new RequestFactory(retrofit, method);
    }
}
