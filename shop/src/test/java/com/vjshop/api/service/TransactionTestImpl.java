package com.vjshop.api.service;

import com.vjshop.api.dao.IMemberDao;
import com.vjshop.generated.db.tables.records.TMemberRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by moss-zc on 2017/5/23.
 *
 * @author yishop term
 * @since 0.1
 */
@Service
public class TransactionTestImpl implements TransactionTest {

    @Resource
    IMemberDao memberDao ;
    @Resource(name = "memberDaoTransactionTestImpl")
    IMemberDao memberDaoTestImpl ;

    @Override
    @Transactional
    public void memberTransactionTest() {
        String random = String.valueOf(Math.round(Math.random()*10000));
        TMemberRecord tMember = memberDao.getUserByAccount("13888888888");
        tMember.setAttributeValue1(String.valueOf(random));
        memberDao.update(tMember);
        throw new RuntimeException("报错了报错了");
    }

    @Override
    @Transactional
    public void memberDaoDSLTest(){
        String random = String.valueOf(Math.round(Math.random()*10000));
        TMemberRecord tMember = memberDaoTestImpl.getUserByAccount("13888888888");
        tMember.setAttributeValue1(String.valueOf(random));
        memberDaoTestImpl.update(tMember);
        throw new RuntimeException("报错了报错了");
    }
}