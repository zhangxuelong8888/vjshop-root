package com.vjshop.api.service;

import com.vjshop.api.entity.TProductCategoryNode;
import com.vjshop.generated.db.tables.records.TProductCategoryRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by moss-zc on 2017/6/15.
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
public class CategoryServiceTest {
    @Resource
    CategoryService categoryService ;

    @Test
    public void findRoots() throws Exception {
        List<TProductCategoryRecord> categoryRecordList = categoryService.findRoots();
        assertNotNull("root获取失败，获取数据为空",categoryRecordList);
        assertEquals("列表数量不正确",categoryRecordList.size(),7);
    }

    @Test
    public void findTree() throws Exception {
        TProductCategoryNode node = categoryService.findTree(0l);
        assertNotNull(node);
        assertEquals(node.getChildren().size() , 7);
    }

    @Test
    public void findChildren() throws Exception {
        List<TProductCategoryRecord> categoryRecordList = categoryService.findChildren(1l,0,7);
        assertNotNull("root获取失败，获取数据为空",categoryRecordList);
        assertNotSame("root获取失败，列表为空",0,categoryRecordList.size());
        assertSame("列表数量不正确",categoryRecordList.size(),7);
    }

}