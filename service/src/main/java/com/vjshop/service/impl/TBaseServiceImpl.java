package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.JooqBaseDao;
import com.vjshop.service.TBaseService;
import org.jooq.UpdatableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * @author ck
 * @date 2017/5/12 0012
 */
public class TBaseServiceImpl<R extends UpdatableRecord<R>, T, ID extends Serializable> implements TBaseService<T, ID> {

    private JooqBaseDao baseDao;

    @Autowired
    protected void setBaseDao(JooqBaseDao<R, T, ID> baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional(readOnly = true)
    public T find(ID id) {
        return (T) baseDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return findList(null, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<T> findList(ID... ids) {
        return baseDao.find(ids);
    }

    @Transactional(readOnly = true)
    public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
        return findList(null, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        return baseDao.findList(null, first, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        return baseDao.findPage(pageable);
    }

    @Transactional(readOnly = true)
    public long count() {
        return count(new Filter[] {});
    }

    @Transactional(readOnly = true)
    public long count(Filter... filters) {
        return baseDao.count(filters);
    }

    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return baseDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(Filter... filters) {
        return baseDao.count(filters) > 0;
    }

    @Transactional
    public T save(T entity) {
        Assert.notNull(entity);
        if(baseDao.isNew(entity)){
            return (T) baseDao.insertAndFetch(entity);
        } else {
            return (T) baseDao.update(entity, null);
        }
    }

    @Transactional
    public T update(T entity) {
        Assert.notNull(entity);
        Assert.isTrue(!baseDao.isNew(entity));
        baseDao.update(entity);
        return entity;
    }

    @Transactional
    public T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity);
        Assert.isTrue(!baseDao.isNew(entity));
        return (T) baseDao.update(entity, ignoreProperties);
    }

    @Transactional
    public T updateSelective(T entity) {
        Assert.notNull(entity);
        Assert.isTrue(!baseDao.isNew(entity));
        baseDao.updateSelective(entity);
        return entity;
    }

    @Transactional
    public void delete(ID id) {
        baseDao.deleteById(id);
    }

    @Transactional
    public void delete(ID... ids) {
        baseDao.deleteById(ids);
    }

    @Transactional
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Transactional
    public void delete(List<T> entities){baseDao.delete(entities);}
}
