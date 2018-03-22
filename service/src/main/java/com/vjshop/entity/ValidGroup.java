
package com.vjshop.entity;

import javax.validation.groups.Default;

/**
 * 验证组
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class ValidGroup {

    /**
     * 保存验证组
     */
    public interface Save extends Default {

    }

    /**
     * 更新验证组
     */
    public interface Update extends Default {

    }

}
