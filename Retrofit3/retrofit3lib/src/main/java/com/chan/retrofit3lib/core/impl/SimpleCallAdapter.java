package com.chan.retrofit3lib.core.impl;

import com.chan.retrofit3lib.core.interfaces.Call;
import com.chan.retrofit3lib.core.interfaces.ICallAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public class SimpleCallAdapter implements ICallAdapter<Call<?>> {

    private Type m_responseType;

    public SimpleCallAdapter(Type responseType) {
        m_responseType = responseType;
    }

    /**
     * @return
     */
    @Override
    public Type getResponseType() {
        return m_responseType;
    }

    @Override
    public <R> Call<R> adapt(Call<R> call) throws IOException {
        return call;
    }
}
