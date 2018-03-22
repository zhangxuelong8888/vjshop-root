package com.vjshop.api.action;

import com.vjshop.AbstractClientTest;
import com.vjshop.api.entity.LoginInfo;
import com.vjshop.api.entity.VResult;
import org.junit.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by moss-zc on 2017/5/22.
 *
 * @author yishop term
 * @since 0.1
 */
public class LoginActionTest extends AbstractClientTest {

    @Test
    public void testLogin(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());


        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("username","13888888888");
        map.add("password","123456");
        //登录
        VResult oo = restTemplate.postForObject(baseUri+"login", map, VResult.class);

        //检测是否登录成功
        assertNotSame("0",oo.get("err"));

        //检测
        LoginInfo ooSession = restTemplate.getForObject(baseUri+"getinfo?token="+((LoginInfo)oo.get("user")).getToken(), LoginInfo.class);
        //确定获取到的session对象存在
        assertNotNull(ooSession);
        //确定使用token取到的是同一个session,无法判断内存地址,只能判断内容是否相同
        assertEquals(((LoginInfo)oo.get("user")).getMemberId(),ooSession.getMemberId());

        //测试登录失败的返回
        map = new LinkedMultiValueMap<>();
        map.add("username","13888888888");
        map.add("password","123456");
    }


}
