package com.chan.retrofit3lib.core.interfaces;

import com.chan.retrofit3lib.Retrofit3;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public interface ICallAdapter<T> {

    /**
     * 返回值的模板参数
     * 比如Call<T> 返回的就是T类型
     * @return
     */
    Type getResponseType();
    <R> T adapt(Call<R> call) throws IOException;

    interface CallAdapterFactory {
        ICallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit3 retrofit);
    }
}
