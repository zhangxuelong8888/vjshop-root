package com.vjshop.api.action;

import com.vjshop.AbstractClientTest;
import com.vjshop.api.entity.LoginInfo;
import com.vjshop.api.entity.VResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotSame;

/**
 * Created by moss-zc on 2017/6/13.
 *
 * @author yishop term
 * @since 0.1
 */


@ContextConfiguration(locations = {
        "classpath:conf/beans-config.xml",
        "classpath:conf/spring-mvc.xml"})
@EnableTransactionManagement
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
public class CaptchaActionTest extends AbstractClientTest{

    @Test
    public void test(){

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("type","2");
        //登录
        VResult oo = restTemplate.postForObject(baseUri+"captcha/get.json", map, VResult.class);

        //检测是否獲取成功
        assertNotSame("0",oo.get("err"));

        //检测
        ResponseEntity ooSession = restTemplate.getForEntity(baseUri+"captcha/img.json", LoginInfo.class);


        //测试登录失败的返回
        map = new LinkedMultiValueMap<>();
        map.add("username","13888888888");
        map.add("password","123456");
    }
}
