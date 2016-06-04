package com.chan.retrofit3;

import com.chan.retrofit3lib.annotation.Get;
import com.chan.retrofit3lib.annotation.Path;
import com.chan.retrofit3lib.annotation.Query;
import com.chan.retrofit3lib.core.interfaces.Call;

/**
 * Created by chan on 16/6/3.
 */
public interface BookApi {

    @Get("/{book}")
    Call<Book> getBook(@Path("book") String path, @Query("price") String price);
}
