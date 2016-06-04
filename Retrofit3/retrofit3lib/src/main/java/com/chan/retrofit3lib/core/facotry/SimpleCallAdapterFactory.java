package com.chan.retrofit3lib.core.facotry;

import com.chan.retrofit3lib.Retrofit3;
import com.chan.retrofit3lib.core.interfaces.Call;
import com.chan.retrofit3lib.core.impl.SimpleCallAdapter;
import com.chan.retrofit3lib.core.interfaces.ICallAdapter;
import com.chan.retrofit3lib.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by chan on 16/6/3.
 */
public class SimpleCallAdapterFactory implements ICallAdapter.CallAdapterFactory {

    private SimpleCallAdapterFactory() {
    }

    /**
     * 获取对应的CallAdapter
     * @param returnType 方法的返回类型
     * @param annotations 注解
     * @param retrofit
     * @return
     */
    @Override
    public ICallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit3 retrofit) {

        //判断下返回类型是否是Parameterized Type的
        //符合这种类型的有 Set<String> List<String>这种的
        if (!(returnType instanceof ParameterizedType)) {
            return null;
        }

        return getCallAdapter(returnType);
    }

    /**
     * 根据返回值类型获得call adapter
     * @param returnType
     * @return
     */
    private ICallAdapter<?> getCallAdapter(Type returnType) {

        //获得它的原始类型 就是去掉模板参数之后的
        Class<?> clazz = Utils.getRawType(returnType);

        //看是否是Call.class  如果不是 那么当前的返回值 是不能用这个CallAdapter进行修饰的
        if(clazz != Call.class) {
            return null;
        }

        //生成简单的CallAdapter
        return new SimpleCallAdapter(
                Utils.getCallResponseType((ParameterizedType) returnType));
    }

    public static ICallAdapter.CallAdapterFactory create() {
        return new SimpleCallAdapterFactory();
    }
}
