package com.vjshop.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
/**
 * Created by moss-zc on 2017/7/24.
 *
 * @author yishop term
 * @since 0.1
 */
public abstract class AbstractJsonSerializer<T> extends JsonSerializer<T> {
    @Override
    public abstract void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException;

    /**
     * 利用反射机制，获取对应的泛型参数
     * @return
     */
    public Class<?> getSuportType(){
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }
}
