package com.vjshop.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by moss-zc on 2017/5/5.
 *  jackson的 record输出
 * @author yishop term
 * @since 0.1
 */
@Component
public class JooqRecordSerializer extends AbstractJsonSerializer<Record> {

    @Override
    public void serialize(Record record, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeObject(record.intoMap());
    }


}
