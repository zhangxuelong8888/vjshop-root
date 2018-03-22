package com.vjshop;

import org.springframework.transaction.TransactionStatus;

import org.jooq.Transaction;

/**
 * @author ck
 * @date 2017/6/29 0029
 */
public class SpringTransaction implements Transaction {

    final TransactionStatus tx;

    SpringTransaction(TransactionStatus tx) {
        this.tx = tx;
    }
}
