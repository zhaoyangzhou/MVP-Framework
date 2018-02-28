package com.example.app.base;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Package: com.example.app.base
 * Class: ResultsDeserializer
 * Description: Json解析器（Retrofit中配置使用）
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class ResultsDeserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement je, Type typeOfT,
                         JsonDeserializationContext context) throws JsonParseException {
        // 转换Json的数据, 获取内部有用的信息
        JsonElement results = je.getAsJsonObject();
        return new Gson().fromJson(results, typeOfT);
    }
}
