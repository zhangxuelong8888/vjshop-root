
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TMemberAttribute;
import com.vjshop.generated.db.tables.TArea;
import com.vjshop.generated.db.tables.TMember;
import com.vjshop.generated.db.tables.TMemberRank;
import com.vjshop.generated.db.tables.records.TMemberRecord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * DAO - 会员
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TMemberDao extends JooqBaseDao<TMemberRecord, com.vjshop.entity.TMember, Long> {

    public TMemberDao() {
        super(TMember.T_MEMBER, com.vjshop.entity.TMember.class);
    }

    @Autowired
    public TMemberDao(Configuration configuration) {
        super(TMember.T_MEMBER, com.vjshop.entity.TMember.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TMember object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TMember> fetchById(Long... values) {
        return fetch(TMember.T_MEMBER.ID, values);
    }

    /**
     * 查询会员详情
     *
     * @param memberId
     * 			  会员ID
     * @return
     * 			  会员
     */
    public com.vjshop.entity.TMember findDetails(Long memberId){
        SelectQuery query = getDslContext().select(TMember.T_MEMBER.fields())
                .from(TMember.T_MEMBER)
                .where(TMember.T_MEMBER.ID.eq(memberId))
                .getQuery();
        addJoin(query);
        List<com.vjshop.entity.TMember> memberList = resultSet2List(query.fetchResultSet());
        if (CollectionUtils.isNotEmpty(memberList)){
            return memberList.get(0);
        }
        return null;
    }

    /**
     * 判断用户名是否存在
     *
     * @param username
     *            用户名(忽略大小写)
     * @return 用户名是否存在
     */
    public boolean usernameExists(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        return getDslContext().fetchExists(TMember.T_MEMBER, TMember.T_MEMBER.USERNAME.eq(username));
    }

    /**
     * 判断E-mail是否存在
     *
     * @param email
     *            E-mail(忽略大小写)
     * @return E-mail是否存在
     */
    public boolean emailExists(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return getDslContext().fetchExists(TMember.T_MEMBER, TMember.T_MEMBER.EMAIL.eq(email));
    }

    /**
     * 查找会员
     *
     * @param loginPluginId
     *            登录插件ID
     * @param openId
     *            openID
     * @return 会员，若不存在则返回null
     */
    public com.vjshop.entity.TMember find(String loginPluginId, String openId) {
        if (StringUtils.isEmpty(loginPluginId) || StringUtils.isEmpty(openId)) {
            return null;
        }
        TMemberRecord record = getDslContext().selectFrom(TMember.T_MEMBER)
                .where(new Condition[]{TMember.T_MEMBER.LOGIN_PLUGIN_ID.eq(loginPluginId), TMember.T_MEMBER.OPEN_ID.eq(openId)})
                .fetchOne();
        return record2Pojo(record);
    }

    /**
     * 根据用户名查找会员
     *
     * @param username
     *            用户名(忽略大小写)
     * @return 会员，若不存在则返回null
     */
    public com.vjshop.entity.TMember findByUsername(String username){
        return fetchOne(TMember.T_MEMBER.USERNAME, username);
    }

    /**
     * 根据E-mail查找会员
     *
     * @param email
     *            E-mail(忽略大小写)
     * @return 会员，若不存在则返回null
     */
    public List<com.vjshop.entity.TMember> findListByEmail(String email){
        return fetch(TMember.T_MEMBER.EMAIL, email);
    }

    /**
     * 查找会员分页
     *
     * @param rankingType
     *            排名类型
     * @param pageable
     *            分页信息
     * @return 会员分页
     */
    public Page<com.vjshop.entity.TMember> findPage(com.vjshop.entity.TMember.RankingType rankingType, Pageable pageable){
        SelectQuery query = getDslContext().select(TMember.T_MEMBER.fields())
                .from(TMember.T_MEMBER)
                .getQuery();
        addJoin(query);
        if (rankingType != null){
            switch (rankingType) {
                case point:
                    query.addOrderBy(TMember.T_MEMBER.POINT.desc());
                    break;
                case balance:
                    query.addOrderBy(TMember.T_MEMBER.BALANCE.desc());
                    break;
                case amount:
                    query.addOrderBy(TMember.T_MEMBER.AMOUNT.desc());
                    break;
            }
        }
        return super.findPage(query, pageable);
    }

    /**
     * 查询会员注册数
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 会员注册数
     */
    public Long registerMemberCount(Date beginDate, Date endDate){
        Timestamp begin = new Timestamp(beginDate.getTime());
        Timestamp end = new Timestamp(endDate.getTime());
        return (getDslContext().selectCount().from(TMember.T_MEMBER)
                .where(TMember.T_MEMBER.CREATE_DATE.between(begin, end))
                .fetchOne(0, Long.class)).longValue();
    }

    /**
     * 清空会员注册项值
     *
     * @param memberAttribute
     *            会员注册项
     */
    public void clearAttributeValue(TMemberAttribute memberAttribute){
        if (memberAttribute == null || memberAttribute.getType() == null || memberAttribute.getPropertyIndex() == null) {
            return;
        }
        String propertyName;
        switch (memberAttribute.getEnumType()) {
            case text:
            case select:
            case checkbox:
                propertyName = com.vjshop.entity.TMember.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
                break;
            default:
                propertyName = String.valueOf(memberAttribute.getType());
                break;
        }
       getDslContext().update(TMember.T_MEMBER).set(getField(propertyName), "").execute();
    }

    public void addJoin(SelectQuery query){
        query.addSelect(TMemberRank.T_MEMBER_RANK.as("memberRankInfo").ID.as("memberRankInfo.id"),
                TMemberRank.T_MEMBER_RANK.as("memberRankInfo").NAME.as("memberRankInfo.name"));
        query.addSelect(TArea.T_AREA.as("areaInfo").ID.as("areaInfo.id"),
                TArea.T_AREA.as("areaInfo").NAME.as("areaInfo.name"),
                TArea.T_AREA.as("areaInfo").FULL_NAME.as("areaInfo.fullName"));

        query.addJoin(TMemberRank.T_MEMBER_RANK.as("memberRankInfo"), JoinType.LEFT_OUTER_JOIN,
                TMemberRank.T_MEMBER_RANK.as("memberRankInfo").ID.eq(TMember.T_MEMBER.MEMBER_RANK)
                        .or(TMember.T_MEMBER.MEMBER_RANK.isNull()
                                .and(TMemberRank.T_MEMBER_RANK.as("memberRankInfo").IS_DEFAULT.eq(true))));
        query.addJoin(TArea.T_AREA.as("areaInfo"), JoinType.LEFT_OUTER_JOIN,
                TArea.T_AREA.as("areaInfo").ID.eq(TMember.T_MEMBER.AREA));
    }
}
