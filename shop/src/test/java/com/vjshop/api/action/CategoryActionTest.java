package com.vjshop.api.action;

import com.vjshop.AbstractClientTest;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by moss-zc on 2017/6/15.
 *
 * @author yishop term
 * @since 0.1
 */
public class CategoryActionTest extends AbstractClientTest {
    @Test
    public void getRoots() throws Exception {

        //登录
        Map oo = restTemplate.getForObject(baseUri + "category/roots.json",  Map.class);

        //检测是否成功
        assertNotSame("0", oo.get("err"));

        List<Map> roots = (List) oo.get("roots");
        assertNotNull("未获取到结果" , roots);
        assertEquals("结果数量错误" , 7 ,roots.size());

    }


    @Test
    public void getChildren() throws Exception {


        Map<String,Object> map = new HashMap<>();
        map.put("rootId",1);
        //登录
        Map oo = restTemplate.getForObject(baseUri + "category/items.json?rootId=1", Map.class,map);

        //检测是否成功
        assertNotSame("0", oo.get("err"));

        List<Map> items = (List) oo.get("items");
        assertNotNull("未获取到结果" , items);
        assertEquals("结果数量错误" , 7 ,items.size());
    }

    @Test
    public void getTree() throws Exception {


        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("rootId","1");
        //登录
        Map oo = restTemplate.postForObject(baseUri + "category/tree.json",new HashMap<>(),  Map.class,new HashMap<>());

        //检测是否成功
        assertNotSame("0", oo.get("err"));

        List<Object> tree = (List<Object>) oo.get("items");
        assertNotNull("未获取到结果" , tree);
        assertEquals("结果数量错误" , 6 ,tree.size());
    }
}
