
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TArea;
import com.vjshop.generated.db.tables.records.TAreaRecord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;

/**
 * Dao 地区
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TAreaDao extends JooqBaseDao<TAreaRecord, com.vjshop.entity.TArea, Long> {

    /** 树路径分隔符 */
    public static final String TREE_PATH_SEPARATOR = ",";

    public TAreaDao() {
        super(TArea.T_AREA, com.vjshop.entity.TArea.class);
    }

    @Autowired
    public TAreaDao(Configuration configuration) {
        super(TArea.T_AREA, com.vjshop.entity.TArea.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TArea object) {
        return object.getId();
    }

    /**
     * 查找顶级地区
     *
     * @param count
     *            数量
     * @return 顶级地区
     */
    public List<com.vjshop.entity.TArea> findRoots(Integer count){
        SelectQuery query = getDslContext().selectFrom(TArea.T_AREA)
                .where(TArea.T_AREA.PARENT.isNull())
                .orderBy(TArea.T_AREA.ORDERS.asc()).getQuery();
        if (count != null){
            query.addLimit(count.intValue());
        }
        ResultSet rs = query.fetchResultSet();
        return resultSet2List(rs);
    }

    /**
     * 查找上级地区
     *
     * @param area
     *            地区
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 上级地区
     */
    public List<com.vjshop.entity.TArea> findParents(com.vjshop.entity.TArea area,
                                                     boolean recursive, Integer count){
        if (area == null || area.getParent() == null) {
            return Collections.emptyList();
        }
        SelectQuery query;
        if (recursive){
            query = getDslContext().selectFrom(TArea.T_AREA)
                    .where(TArea.T_AREA.ID.in(area.getParentIds()))
                    .orderBy(TArea.T_AREA.GRADE.asc()).getQuery();
        } else {
            query = getDslContext().selectFrom(TArea.T_AREA)
                    .where(TArea.T_AREA.ID.eq(area.getParent()))
                    .orderBy(TArea.T_AREA.ORDERS.asc()).getQuery();
        }
        if (count != null){
            query.addLimit(count.intValue());
        }
        ResultSet rs = query.fetchResultSet();
        return resultSet2List(rs);
    }

    /**
     * 查找下级地区
     *
     * @param area
     *            地区
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 下级地区
     */
    public List<com.vjshop.entity.TArea> findChildren(com.vjshop.entity.TArea area,
                                                      boolean recursive, Integer count){
        if (area == null) {
            return Collections.emptyList();
        }
        SelectQuery query;
        if (recursive){
            query = getDslContext().selectFrom(TArea.T_AREA)
                    .where(TArea.T_AREA.TREE_PATH.like("%" + com.vjshop.entity.TArea.TREE_PATH_SEPARATOR + area.getId() + com.vjshop.entity.TArea.TREE_PATH_SEPARATOR + "%")
                            .and(TArea.T_AREA.PARENT.in(area.getParentIds())))
                    .orderBy(TArea.T_AREA.ORDERS.asc()).getQuery();
            if (count != null){
                query.addLimit(count.intValue());
            }
            ResultSet rs = query.fetchResultSet();
            List<com.vjshop.entity.TArea> areaList = resultSet2List(rs);
            sort(areaList);
            return areaList;
        } else {
            query = getDslContext().selectFrom(TArea.T_AREA)
                    .where(TArea.T_AREA.PARENT.eq(area.getId()))
                    .orderBy(TArea.T_AREA.ORDERS.asc()).getQuery();
            if (count != null){
                query.addLimit(count.intValue());
            }
            ResultSet rs = query.fetchResultSet();
            return resultSet2List(rs);
        }
    }

    /**
     * 排序地区
     *
     * @param areas
     *            地区
     */
    private void sort(List<com.vjshop.entity.TArea> areas) {
        if (CollectionUtils.isEmpty(areas)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
        for (com.vjshop.entity.TArea area : areas) {
            orderMap.put(area.getId(), area.getOrders());
        }
        Collections.sort(areas, new Comparator<com.vjshop.entity.TArea>() {
            @Override
            public int compare(com.vjshop.entity.TArea area1,
                               com.vjshop.entity.TArea area2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(area1.getParentIds(), area1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(area2.getParentIds(), area2.getId());
                Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
                Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
                CompareToBuilder compareToBuilder = new CompareToBuilder();
                while (iterator1.hasNext() && iterator2.hasNext()) {
                    Long id1 = iterator1.next();
                    Long id2 = iterator2.next();
                    Integer order1 = orderMap.get(id1);
                    Integer order2 = orderMap.get(id2);
                    compareToBuilder.append(order1, order2).append(id1, id2);
                    if (!iterator1.hasNext() || !iterator2.hasNext()) {
                        compareToBuilder.append(area1.getGrade(), area2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }

}
