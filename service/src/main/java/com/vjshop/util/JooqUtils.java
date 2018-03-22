
package com.vjshop.util;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TLog;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Field;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils - JOOQ
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class JooqUtils {

    /**
     * 获取属性名对应字段，驼峰转下划线
     *
     * @param property
     *          属性名
     * @return Field
     *          字段
     */
    public static Field getField(String property){
        if (property.indexOf(".") >= 0){
            int li = property.lastIndexOf(".") + 1;
            property = property.substring(0, li) + hump2underline(property.substring(li));
        } else {
            property = hump2underline(property);
        }
        return DSL.field(property);
    }

    /**
     * 获取字段，下划线转驼峰
     *
     * @param table
     *          表
     * @param alias
     *          别名
     * @return
     *          字段
     */
    public static Field<?>[] getFields(Table table, String alias){
        if (StringUtils.isBlank(alias)){
            return table.fields();
        }
        Field[] fields = table.fields();
        int len = fields.length;
        Field[] nFields = new Field[len];
        for (int i = 0;i < len;i++){
            nFields[i] = fields[i].as(alias + "." + underline2hump(fields[i].getName()));
        }
        return nFields;
    }

    /**
     * 获取属性名，下划线转驼峰
     *
     * @param field
     *          字段
     * @return
     *          属性名
     */
    public static String getProperty(Field field){
        return underline2hump(field.getName());
    }

    /**
     * 转换为查询条件
     *
     * @param query
     *            查询条件
     * @param filters
     *            筛选
     * @return 查询条件
     */
    public static SelectQuery toFilters(SelectQuery query, List<Filter> filters) {
        if (CollectionUtils.isEmpty(filters)) {
            return query;
        }
        for (Filter filter : filters) {
            if (filter == null) {
                continue;
            }
            String property = filter.getProperty();
            Filter.Operator operator = filter.getOperator();
            Object value = filter.getValue();
            Boolean ignoreCase = filter.getIgnoreCase();
            switch (operator) {
                case eq:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
                            query.addConditions(DSL.lower(getField(property)).eq(((String) value).toLowerCase()));
                        } else {
                            query.addConditions(getField(property).eq(value));
                        }
                    } else {
                        query.addConditions(getField(property).isNull());
                    }
                    break;
                case ne:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
                            query.addConditions(DSL.lower(getField(property)).ne(((String) value).toLowerCase()));
                        } else {
                            query.addConditions(getField(property).ne(value));
                        }
                    } else {
                        query.addConditions(getField(property).isNotNull());
                    }
                    break;
                case gt:
                    if (value instanceof Number) {
                        query.addConditions(getField(property).gt(value));
                    }
                    break;
                case lt:
                    if (value instanceof Number) {
                        query.addConditions(getField(property).lt(value));
                    }
                    break;
                case ge:
                    if (value instanceof Number) {
                        query.addConditions(getField(property).ge(value));
                    }
                    break;
                case le:
                    if (value instanceof Number) {
                        query.addConditions(getField(property).le(value));
                    }
                    break;
                case like:
                    if (value instanceof String) {
                        if (BooleanUtils.isTrue(ignoreCase)) {
                            query.addConditions(DSL.lower(getField(property)).like(((String) value).toLowerCase()));
                        } else {
                            query.addConditions(getField(property).like((String) value));
                        }
                    }
                    break;
                case in:
                    query.addConditions(getField(property).in(value));
                    break;
                case isNull:
                    query.addConditions(getField(property).isNull());
                    break;
                case isNotNull:
                    query.addConditions(getField(property).isNotNull());
                    break;
            }
        }
        return query;
    }

    /**
     * 转换为查询条件
     *
     * @param query
     *            查询条件
     * @param orders
     *            排序
     * @return 查询条件
     */
    public static SelectQuery toOrders(SelectQuery query, List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return query;
        }
        for (Order order : orders) {
            if (order == null) {
                continue;
            }
            String property = order.getProperty();
            Order.Direction direction = order.getDirection();
            switch (direction) {
                case desc:
                    query.addOrderBy(getField(property).desc());
                    break;
                default:
                    query.addOrderBy(getField(property).asc());
                    break;
            }
        }
        return query;
    }
    
    /**
     * 驼峰转下划线
     *
     * @param str 驼峰法字符串
     * @return
     *          下划法字符串
     */
    public static String hump2underline(String str){
        if(StringUtils.isBlank(str)){
            return "";
        }
        str = str.substring(0, 1).toUpperCase().concat(str.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == str.length()?"":"_");
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param str 下划法字符串
     * @return
     *          驼峰法字符串
     */
    public static String underline2hump(String str){
        if(StringUtils.isBlank(str)){
            return "";
        }
        int first_ = str.indexOf("_");
        if (first_ < 0) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(str.substring(0, first_).toLowerCase());
        Pattern pattern = Pattern.compile("(_)([A-Za-z\\d]+)?");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String word = matcher.group();
            if (word.length() > 1){
                sb.append(word.substring(1, 2).toUpperCase())
                        .append(word.substring(2).toLowerCase());
            } else {
                sb.append(word);
            }
        }
        return sb.toString();
    }

    /**
     * 拷贝对象属性
     *
     * @param source
     *            源
     * @param target
     *            目标
     */
    public static void copyProperties(Object source, Object target) {
        Assert.notNull(source);
        Assert.notNull(target);
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(target);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (readMethod == null || writeMethod == null) {
                continue;
            }
            try {
                Object sourceValue = readMethod.invoke(source);
                if (sourceValue != null){
                    writeMethod.invoke(target, sourceValue);
                } else {//如果源对象没有值，将目标属性值给源属性，保证源属性与目标属性统一
                    Object targetValue = readMethod.invoke(target);
                    writeMethod.invoke(source, targetValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 拷贝对象属性
     *
     * @param source
     *            源
     * @param target
     *            目标
     * @param ignoreProperties
     *            忽略属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        Assert.notNull(source);
        Assert.notNull(target);
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(target);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (ArrayUtils.contains(ignoreProperties, propertyName) || readMethod == null || writeMethod == null) {
                continue;
            }
            try {
                Object sourceValue = readMethod.invoke(source);
                writeMethod.invoke(target, sourceValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
