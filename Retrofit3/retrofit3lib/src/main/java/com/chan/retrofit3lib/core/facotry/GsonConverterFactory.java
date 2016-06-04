package com.chan.retrofit3lib.core.facotry;

import com.chan.retrofit3lib.core.impl.GsonConverter;
import com.chan.retrofit3lib.core.interfaces.IConverter;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public class GsonConverterFactory implements IConverter.IConverterFactory {

    private Gson m_gson;

    private GsonConverterFactory() {
        m_gson = new Gson();
    }

    public Gson getGson() {
        return m_gson;
    }

    public void setGson(Gson gson) {
        m_gson = gson;
    }

    @Override
    public IConverter create(Type responseType, Annotation[] annotations) {
        return new GsonConverter(m_gson, responseType);
    }

    public static IConverter.IConverterFactory create() {
        return new GsonConverterFactory();
    }
}
