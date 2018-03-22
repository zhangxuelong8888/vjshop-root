package com.vjshop.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vjshop.entity.TreeNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by moss-zc on 2017/5/5.
 *  jackson序列化工具，将树对象转换为map输出
 * @author yishop term
 * @since 0.1
 */
@Component
public class JooqRecordTreeNodeSerializer extends AbstractJsonSerializer<TreeNode> {

    @Override
    public void serialize(TreeNode record, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        Map<String,Object> map = record.intoMap();
        map.put("children", record.getChildren());
        jsonGenerator.writeObject(map);
    }


}
