package com.vjshop.api.dao;

import com.vjshop.api.service.TransactionTest;
import com.vjshop.generated.db.tables.records.TMemberRecord;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

/**
 * Created by moss-zc on 2017/5/23.
 *
 * @author yishop term
 * @since 0.1
 */
public class JooqTransactionTest {
    Logger log = LoggerFactory.getLogger(JooqTransactionTest.class);

    @Resource
    TransactionTest transactionTest;

    @Resource(name = "memberDaoImpl")
    IMemberDao memberDaoImpl ;
    @Resource(name = "memberDaoTransactionTestImpl")
    IMemberDao memberDaoTestImpl ;

    @Test
    @Rollback
    public void testJooqTransaction(){

        try {
            transactionTest.memberTransactionTest();
        }catch (RuntimeException e){
            log.debug("事务报错了");
        }
        TMemberRecord tMember = memberDaoImpl.getUserByAccount("13888888888");
        Assert.assertNull(tMember.getAttributeValue1());
    }

    @Test
    @Rollback
    public void testJooqTestTransaction(){

        try {
            transactionTest.memberTransactionTest();
        }catch (RuntimeException e){
            log.debug("事务报错了");
        }
        TMemberRecord tMember = memberDaoTestImpl.getUserByAccount("13888888888");
        Assert.assertNull(tMember.getAttributeValue1());
    }

}
