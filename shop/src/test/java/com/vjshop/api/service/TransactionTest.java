package com.vjshop.api.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by moss-zc on 2017/5/23.
 *
 * @author yishop term
 * @since 0.1
 */
public interface TransactionTest {
    void memberTransactionTest();

    @Transactional
    void memberDaoDSLTest();
}
