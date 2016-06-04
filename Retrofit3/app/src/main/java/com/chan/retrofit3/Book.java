package com.chan.retrofit3;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 16/6/3.
 */
public class Book {

    /**
     * name : c++ reference
     */

    @SerializedName("name")
    private String name;

    public static Book objectFromData(String str) {

        return new Gson().fromJson(str, Book.class);
    }

    public static List<Book> arrayBookFromData(String str) {

        Type listType = new TypeToken<ArrayList<Book>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
