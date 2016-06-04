package com.chan.retrofit3lib.core.interfaces;

import java.io.IOException;

/**
 * Created by chan on 16/6/3.
 */
public interface Call<T> {
    T execute() throws IOException;
}
