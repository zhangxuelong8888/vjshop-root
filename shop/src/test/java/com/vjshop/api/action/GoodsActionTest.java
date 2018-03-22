package com.vjshop.api.action;

import com.vjshop.AbstractClientTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Created by moss-zc on 2017/7/25.
 *
 * @author yishop term
 * @since 0.1
 */
public class GoodsActionTest extends AbstractClientTest {

    @Test
    public void detailTest(){
        Map<String,Object> map = new HashMap<>();
        map.put("username","13888888888" );
        map.put("password","123456" );
        //登录
        Map oo = restTemplate.getForObject(baseUri + "login.json", Map.class,map);
        oo.get("token");

        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",1);

        //登录
        Map oo1 = restTemplate.getForObject(baseUri + "goods/auth/detail.json", Map.class,map1);

        //检测是否成功
        assertNotSame("0", oo1.get("err"));

        List<Map> items = (List) oo1.get("items");
        assertNotNull("未获取到结果" , items);
        assertEquals("结果数量错误" , 7 ,items.size());

    }

}
