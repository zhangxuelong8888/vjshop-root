package com.vjshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moss-zc on 2017/5/22.
 *
 * @author yishop term
 * @since 0.1
 */

@RunWith(JUnit4ClassRunner.class)
public class AbstractClientTest {

    ObjectMapper objectMapper; //JSON
    public String baseUri = "http://localhost:8090/api/";
    private static Server server;
    public RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(); //需要添加jackson jar包
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    }


    @BeforeClass
    public static void beforeClass() throws Exception {
        //创建一个server
        server = new Server(8090);
        WebAppContext context = new WebAppContext();
        String webapp = "src/main/webapp";
        context.setDescriptor(webapp + "/WEB-INF/web.xml");  //指定web.xml配置文件
        context.setResourceBase(webapp);  //指定webapp目录
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        server.setHandler(context);
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); //当测试结束时停止服务器
    }
}
