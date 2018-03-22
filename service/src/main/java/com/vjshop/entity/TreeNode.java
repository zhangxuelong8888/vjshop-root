package com.vjshop.entity;

import org.jooq.Record;

import java.util.List;

/**
 * Created by moss-zc on 2017/6/19.
 *
 * @author yishop term
 * @since 0.1
 */
public interface TreeNode extends Record {

    /**
     * 获取所有的子对象
     * @return 获取到的子对象
     */
    List<? extends TreeNode> getChildren();
}
