package com.chan.retrofit3lib.core.impl;

import com.chan.retrofit3lib.core.interfaces.IConverter;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public class GsonConverter<T> implements IConverter<T> {
    private Gson m_gson;
    private Type m_type;

    public GsonConverter(Gson gson, Type type) {
        m_gson = gson;
        m_type = type;
    }

    @Override
    public T convert(Response response) throws IOException {
        return (T) m_gson.fromJson(response.body().charStream(), m_type);
    }
}
