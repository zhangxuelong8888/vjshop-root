package com.vjshop.service;

import com.vjshop.entity.TRole;

import java.util.List;

/**
 * Service - 角色
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TRoleService extends TBaseService<TRole, Long> {

    /**
     * 根据roleId查询有没有对应的用户
     * @param id
     * @return
     */
    Boolean hasUser(Long id);

    /**
     * 根据adminId找到对应的所有角色
     * @param adminId
     * @return
     */
    List<TRole> findRolesByAdminId(Long adminId);
}