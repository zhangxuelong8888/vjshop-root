package com.vjshop;

import com.vjshop.dao.TMemberDao;
import com.vjshop.entity.CkTest;
import com.vjshop.generated.db.tables.CkTest1;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.TProductNotify;
import com.vjshop.generated.db.tables.daos.CkTest2Dao;
import com.vjshop.generated.db.tables.records.CkTest1Record;
import com.vjshop.util.JooqUtils;
import com.vjshop.util.SpringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.junit.Assert;
import org.junit.Test;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vjshop.generated.db.Tables.CK_TEST1;
import static com.vjshop.generated.db.tables.CkTest2.CK_TEST2;

/**
 * @author ck
 * @date 2017/5/3 0003
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mvc.xml",
        "classpath:applicationContext-shiro.xml"})
@WebAppConfiguration
public class JOOQTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private TMemberDao tMemberDao;

//    @Autowired
//    private CkTest2Dao ckTest2Dao;

    @Test
    @Rollback(false)
    public void insert() {
        CkTest1Record ckTest1Record = new CkTest1Record();
        ckTest1Record.setId(1);
        ckTest1Record.setName("ck");
        int count = dslContext.newRecord(CK_TEST1, ckTest1Record).insert();
        System.out.println("insert success:" + count);
        //or
        /*dslContext.insertInto(Tables.CK_TEST1)
                .set(CkTest1.CK_TEST1.ID, 2)
                .set(CkTest1.CK_TEST1.NAME, "zn")
                .execute();*/
    }

    @Test
    @Rollback(false)
    public void update() {
        /*int count = dslContext.update(CK_TEST1)
                .set(CkTest1.CK_TEST1.NAME, "ckk")
                .where(CkTest1.CK_TEST1.ID.eq((Integer.valueOf(1))))
                .execute();*/
        CkTest1Record ckTest1Record = new CkTest1Record();
        ckTest1Record.setId(1);
        ckTest1Record.setName("ckqqq");

        com.vjshop.generated.db.tables.pojos.CkTest2 ckTest2 = new com.vjshop.generated.db.tables.pojos.CkTest2();
        ckTest2.setId(1);
        ckTest2.setUsername("bb");
//        ckTest2Dao.update(ckTest2);

        System.out.println("update success:");

    }

    @Test
    @Rollback(false)
    public void delete() {
        int count = dslContext.deleteFrom(CK_TEST1)
                .where(CkTest1.CK_TEST1.ID.eq(Integer.valueOf(1)))
                .execute();
        System.out.println("delete success:" + count);
    }

    @Test
    public void load() {
        SelectQuery query = this.dslContext.select(TProductNotify.T_PRODUCT_NOTIFY.fields()).from(TProductNotify.T_PRODUCT_NOTIFY).getQuery();

        query.addConditions(TProductNotify.T_PRODUCT_NOTIFY.MEMBER.eq(1L));
        query.addJoin(TProduct.T_PRODUCT, JoinType.JOIN, TProduct.T_PRODUCT.ID.eq(TProductNotify.T_PRODUCT_NOTIFY.PRODUCT));
        query.addJoin(TGoods.T_GOODS, JoinType.JOIN, TProduct.T_PRODUCT.GOODS.eq(TGoods.T_GOODS.ID));
        query.addConditions(TGoods.T_GOODS.IS_MARKETABLE.eq(true));
//        if (isOutOfStock != null) {
        SelectQuery subQuery = null;
//            if (isOutOfStock) {
        subQuery = this.dslContext.select(TProduct.T_PRODUCT.ID)
                .from(TProduct.T_PRODUCT)
                .where(TProduct.T_PRODUCT.ID.eq(TProductNotify.T_PRODUCT_NOTIFY.PRODUCT)).and(TProduct.T_PRODUCT.STOCK.le(TProduct.T_PRODUCT.ALLOCATED_STOCK)).getQuery();
//            } else {
//                subQuery = this.dslContext.select(TProduct.T_PRODUCT.ID)
//                        .from(TProduct.T_PRODUCT)
//                        .where(TProduct.T_PRODUCT.ID.eq(TProductNotify.T_PRODUCT_NOTIFY.PRODUCT)).and(TProduct.T_PRODUCT.STOCK.gt(TProduct.T_PRODUCT.ALLOCATED_STOCK)).getQuery();
//            }
        query.addConditions(TProductNotify.T_PRODUCT_NOTIFY.PRODUCT.in(subQuery));
//        }
        query.addConditions(TProductNotify.T_PRODUCT_NOTIFY.HAS_SENT.eq(true));

        System.out.println(query.getSQL());
    }

    @Test
    public void list() {
        /*List<CkTest1Record> ckTest1Records = dslContext.selectFrom(CkTest1.CK_TEST1)
                .fetchInto(CkTest1Record.class);
        for (CkTest1Record record : ckTest1Records) {
            System.out.println("list id:" + record.getId());
        }*/


        /*Result<Record5<Integer, String, Integer, Integer, String>> record4s =
                dslContext.select(CK_TEST1.ID, CK_TEST1.NAME, CK_TEST2.ID.as("ckTest2.id"), CK_TEST2.PID.as("ckTest2.pid"), CK_TEST2.USERNAME.as("ckTest2.username"))
                        .from(CK_TEST1)
                        .join(CK_TEST2).on(CK_TEST1.ID.eq(CK_TEST2.PID))
                        .fetch();
        List<CkTest> ckTests = record4s.into(CkTest.class);
        System.out.println(ckTests.size());*/

        JdbcMapper mapper = JdbcMapperFactory.newInstance().addKeys("id").newMapper(CkTest.class);

        Field<?>[] fields = CK_TEST1.fields();
        List<Field<?>> fieldList = new ArrayList<>();
        fieldList.addAll((Arrays.asList(fields)));

        Table<?> from = CK_TEST1;
        List<GroupField> groupBy = new ArrayList<>();
        List<SortField<?>> orderBy = new ArrayList<>();

        if (true) {
            fieldList.add(CK_TEST2.ID.as("ckTest2.id"));
            fieldList.add(CK_TEST2.PID.as("ckTest2.pid"));
            fieldList.add(CK_TEST2.USERNAME.as("ckTest2.username"));

            from = from.join(CK_TEST2).on(CK_TEST1.ID.eq(CK_TEST2.PID));

            groupBy.add(CK_TEST1.ID);
            orderBy.add(CK_TEST1.ID.desc());

        }

        ResultSet rs =
//                dslContext.select(CK_TEST1.ID, CK_TEST1.NAME, CK_TEST2.ID.as("ckTest2.id"), CK_TEST2.PID.as("ckTest2.pid"), CK_TEST2.USERNAME.as("ckTest2.username"))

                dslContext.select(fieldList)
                .from(from)
                .groupBy(groupBy)
                .orderBy(orderBy)
                .fetchResultSet();

        try {
            Stream<CkTest> stream = mapper.stream(rs);
            List<CkTest> result = stream.collect(Collectors.toList());
            System.out.println(result.size());
        } catch (SQLException e) {
            throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
        }
    }

    @Test
    public void testJoin(){
        SelectQuery query = dslContext.select(CK_TEST1.fields()).from(CK_TEST1).getQuery();

        //别名对应属性名（下划线转驼峰）
        query.addSelect(JooqUtils.getFields(CK_TEST2, "ckTest2"));
        //关联查询
        query.addJoin(CK_TEST2, JoinType.LEFT_OUTER_JOIN, CK_TEST1.ID.eq(CK_TEST2.PID));

        ResultSet rs = query.fetchResultSet();
            JdbcMapper mapper = JdbcMapperFactory.newInstance().addKeys("id").newMapper(CkTest.class);
        Stream<CkTest> stream = null;
        try {
            stream = mapper.stream(rs);
        } catch (SQLException e) {
            throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
        }
        List<CkTest> result = stream.collect(Collectors.toList());
        Assert.assertNotNull(result);
        Assert.assertEquals("一对多查询失败,数量不符",result.get(0).getCkTest2().size(),2);

    }


}
