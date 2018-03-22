package com.vjshop.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by moss-zc on 2017/6/19.
 *  第三方json格式化注册
 * @author yishop term
 * @since 0.1
 */
@Component
public class CustemObjectMapper extends ObjectMapper  implements InitializingBean {


    @Resource
    ListableBeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String,AbstractJsonSerializer> abstractJsonSerializerList = beanFactory.getBeansOfType(AbstractJsonSerializer.class);
        SimpleModule customModule = new SimpleModule(Version.unknownVersion());
        for(AbstractJsonSerializer serializer : abstractJsonSerializerList.values()) {
            customModule.addSerializer(serializer.getSuportType(), serializer);
        }
        registerModule(customModule);
    }
}
