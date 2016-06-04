package com.chan.retrofit3;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chan.retrofit3lib.Retrofit3;
import com.chan.retrofit3lib.core.facotry.GsonConverterFactory;
import com.chan.retrofit3lib.core.interfaces.Call;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //新建一个retrofit3 对象 不过我们没有指定CallAdapterFactory
        //因为我们的接口方法直接返回的Call<T>类型 无需修饰
        Retrofit3 retrofit3 = new Retrofit3.Builder()
                .baseUri("http://192.168.1.195:8080")
                .converterFactory(GsonConverterFactory.create()).build();

        //新建一个BookApi对象
        final BookApi bookApi = (BookApi) retrofit3.create(BookApi.class);


        findViewById(R.id.id_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //这里对应一次网络请求
                        Call<Book> bookCall = bookApi.getBook("json", "100");
                        try {

                            //同步调用 而不是采用异步的方式
                            Book book = bookCall.execute();

                            //获取到了Book对象 之后同步到UI线程展示一下它
                            Message message = m_handler.obtainMessage();
                            message.obj = book;
                            m_handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


    /**
     *
     */
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Book book = (Book) msg.obj;
            Toast.makeText(MainActivity.this, book.getName(), Toast.LENGTH_SHORT).show();
        }
    };
}
