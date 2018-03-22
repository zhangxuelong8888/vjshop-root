package com.vjshop;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

/**
 * @author ck
 * @date 2017/6/29 0029
 */
public class SpringTransactionProvider implements TransactionProvider {

    private static final JooqLogger log = JooqLogger.getLogger(SpringTransactionProvider.class);

    @Autowired
    DataSourceTransactionManager txMgr;

    /**
     * Begin a new transaction.
     * <p>
     * This method begins a new transaction with a {@link Configuration} scoped
     * for this transaction. The resulting {@link Transaction} object may be
     * used by implementors to identify the transaction when
     * {@link #commit(TransactionContext)} or
     * {@link #rollback(TransactionContext)} is called.
     *
     * @param ctx@throws DataAccessException Any exception issued by the underlying
     *                   database.
     */
    @Override
    public void begin(TransactionContext ctx) throws DataAccessException {
        log.info("Begin transaction");

        // This TransactionProvider behaves like jOOQ's DefaultTransactionProvider,
        // which supports nested transactions using Savepoints
        TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition(PROPAGATION_NESTED));
        ctx.transaction(new SpringTransaction(tx));
    }

    /**
     * @param ctx@throws DataAccessException Any exception issued by the underlying
     *                   database.
     */
    @Override
    public void commit(TransactionContext ctx) throws DataAccessException {
        log.info("commit transaction");

        txMgr.commit(((SpringTransaction) ctx.transaction()).tx);
    }

    /**
     * @param ctx@throws DataAccessException Any exception issued by the underlying
     *                   database.
     */
    @Override
    public void rollback(TransactionContext ctx) throws DataAccessException {
        log.info("rollback transaction");

        txMgr.rollback(((SpringTransaction) ctx.transaction()).tx);
    }
}
