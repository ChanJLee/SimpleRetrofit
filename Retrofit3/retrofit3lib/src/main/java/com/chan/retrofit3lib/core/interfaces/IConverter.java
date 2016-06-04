package com.chan.retrofit3lib.core.interfaces;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public interface IConverter<T> {
    /**
     * @param response
     * @return
     * @throws IOException
     */
    T convert(Response response) throws IOException;


    /**
     * Created by chan on 16/6/3.
     */
    interface IConverterFactory {

        public abstract IConverter create(Type responseType, Annotation[] annotations);
    }
}
