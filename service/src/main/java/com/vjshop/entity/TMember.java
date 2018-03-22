
package com.vjshop.entity;

import com.vjshop.util.JsonUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 会员
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TMember implements Serializable {

    private static final long serialVersionUID = -57920042;

    /**
     * 性别
     */
    public enum Gender {

        /** 男 */
        male,

        /** 女 */
        female
    }

    /**
     * 排名类型
     */
    public enum RankingType {

        /** 积分 */
        point,

        /** 余额 */
        balance,

        /** 消费金额 */
        amount
    }

    /** "身份信息"属性名称 */
    public static final String PRINCIPAL_ATTRIBUTE_NAME = "MemberInterceptor" + ".PRINCIPAL";

    /** "用户名"Cookie名称 */
    public static final String USERNAME_COOKIE_NAME = "username";

    /** "昵称"Cookie名称 */
    public static final String NICKNAME_COOKIE_NAME = "nickname";

    /** 会员注册项值属性个数 */
    public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

    /** 会员注册项值属性名称前缀 */
    public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

    /** 最大收藏商品数 */
    public static final Integer MAX_FAVORITE_COUNT = 10;

    /** 会员等级 */
    private TMemberRank memberRankInfo;

    /** 地区 */
    private TArea areaInfo;

    /** 评论数量 */
    private Long reviewCount;

    /** 咨询数量 */
    private Long consultationCount;

    /** 收藏商品数量 */
    private Long favoriteGoodsCount;

    /** 购物车 */
    private TCart cart;

    /** 订单 */
    private Set<TOrder> orders = new HashSet<TOrder>();

    /** 支付记录 */
    private Set<TPaymentLog> paymentLogs = new HashSet<TPaymentLog>();

    /** 预存款记录 */
    private Set<TDepositLog> depositLogs = new HashSet<TDepositLog>();

    /** 优惠码 */
    private Set<TCouponCode> couponCodes = new HashSet<TCouponCode>();

    /** 收货地址 */
    private Set<TReceiver> receivers = new HashSet<TReceiver>();

    /** 评论 */
    private Set<TReview> reviews = new HashSet<TReview>();

    /** 咨询 */
    private Set<TConsultation> consultations = new HashSet<TConsultation>();

    /** 收藏货品 */
    private Set<TGoods> favoriteGoods = new HashSet<TGoods>();

    /** 到货通知 */
    private Set<TProductNotify> productNotifies = new HashSet<TProductNotify>();

    /** 接收的消息 */
    private Set<TMessage> inMessages = new HashSet<TMessage>();

    /** 发送的消息 */
    private Set<TMessage> outMessages = new HashSet<TMessage>();

    /** 积分记录 */
    private Set<TPointLog> pointLogs = new HashSet<TPointLog>();

    /**
     * 获取性别枚举
     * @return
     *          性别
     */
    public Gender getEnumGender(){
        return this.gender == null ? null : Gender.values()[this.gender.intValue()];
    }

    /**
     * 获取会员等级
     *
     * @return 会员等级
     */
    public TMemberRank getMemberRankInfo() {
        return memberRankInfo;
    }
    /**
     * 设置会员等级
     *
     * @param memberRankInfo
     *            会员等级
     */
    public void setMemberRankInfo(TMemberRank memberRankInfo) {
        this.memberRankInfo = memberRankInfo;
    }
    /**
     * 获取地区
     *
     * @return 地区
     */
    public TArea getAreaInfo() {
        return areaInfo;
    }
    /**
     * 设置地区
     *
     * @param areaInfo
     *            地区
     */
    public void setAreaInfo(TArea areaInfo) {
        this.areaInfo = areaInfo;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getConsultationCount() {
        return consultationCount;
    }

    public void setConsultationCount(Long consultationCount) {
        this.consultationCount = consultationCount;
    }

    public Long getFavoriteGoodsCount() {
        return favoriteGoodsCount;
    }

    public void setFavoriteGoodsCount(Long favoriteGoodsCount) {
        this.favoriteGoodsCount = favoriteGoodsCount;
    }

    public SafeKey getSafeKey() {
        SafeKey safeKey = new SafeKey();
        safeKey.setValue(this.getSafeKeyValue());
        safeKey.setExpire(this.getSafeKeyExpire());
        return safeKey;
    }

    public void setSafeKey(SafeKey safeKey) {
        this.setSafeKeyValue(safeKey != null ? safeKey.getValue() : null);
        this.setSafeKeyExpire(safeKey != null ? safeKey.getExpire() : null);
    }

    public TCart getCart() {
        return cart;
    }

    public void setCart(TCart cart) {
        this.cart = cart;
    }

    public Set<TOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<TOrder> orders) {
        this.orders = orders;
    }

    public Set<TPaymentLog> getPaymentLogs() {
        return paymentLogs;
    }

    public void setPaymentLogs(Set<TPaymentLog> paymentLogs) {
        this.paymentLogs = paymentLogs;
    }

    public Set<TDepositLog> getDepositLogs() {
        return depositLogs;
    }

    public void setDepositLogs(Set<TDepositLog> depositLogs) {
        this.depositLogs = depositLogs;
    }

    public Set<TCouponCode> getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(Set<TCouponCode> couponCodes) {
        this.couponCodes = couponCodes;
    }

    public Set<TReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<TReceiver> receivers) {
        this.receivers = receivers;
    }

    public Set<TReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<TReview> reviews) {
        this.reviews = reviews;
    }

    public Set<TConsultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(Set<TConsultation> consultations) {
        this.consultations = consultations;
    }

    public Set<TGoods> getFavoriteGoods() {
        return favoriteGoods;
    }

    public void setFavoriteGoods(Set<TGoods> favoriteGoods) {
        this.favoriteGoods = favoriteGoods;
    }

    public Set<TProductNotify> getProductNotifies() {
        return productNotifies;
    }

    public void setProductNotifies(Set<TProductNotify> productNotifies) {
        this.productNotifies = productNotifies;
    }

    public Set<TMessage> getInMessages() {
        return inMessages;
    }

    public void setInMessages(Set<TMessage> inMessages) {
        this.inMessages = inMessages;
    }

    public Set<TMessage> getOutMessages() {
        return outMessages;
    }

    public void setOutMessages(Set<TMessage> outMessages) {
        this.outMessages = outMessages;
    }

    public Set<TPointLog> getPointLogs() {
        return pointLogs;
    }

    public void setPointLogs(Set<TPointLog> pointLogs) {
        this.pointLogs = pointLogs;
    }

    /**
     * 获取会员注册项值
     *
     * @param memberAttribute
     *            会员注册项
     * @param propertyIndex
     *            属性序号
     * @return 会员注册项值
     */
    @Transient
    public Object getAttributeValue(TMemberAttribute.Type type, Integer propertyIndex) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case name:
                return getName();
            case gender:
                return getGender();
            case birth:
                return getBirth();
            case area:
                return getArea();
            case address:
                return getAddress();
            case zipCode:
                return getZipCode();
            case phone:
                return getPhone();
            case mobile:
                return getMobile();
            case text:
            case select:
                if (propertyIndex != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + propertyIndex;
                        return PropertyUtils.getProperty(this, propertyName);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                break;
            case checkbox:
                if (propertyIndex != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + propertyIndex;
                        String propertyValue = (String) PropertyUtils.getProperty(this, propertyName);
                        if (StringUtils.isNotEmpty(propertyValue)) {
                            return JsonUtils.toObject(propertyValue, List.class);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                break;
        }
        return null;
    }

    /**
     * 设置会员注册项值
     *
     * @param memberAttribute
     *            会员注册项
     * @param propertyIndex
     *            属性序号
     * @param memberAttributeValue
     *            会员注册项值
     */
    @Transient
    public void setAttributeValue(TMemberAttribute.Type type, Integer propertyIndex, Object memberAttributeValue) {
        if (type == null) {
            return;
        }
        switch (type) {
            case name:
                if (memberAttributeValue instanceof String || memberAttributeValue == null) {
                    setName((String) memberAttributeValue);
                }
                break;
            case gender:
                if (memberAttributeValue instanceof Integer || memberAttributeValue == null) {
                    setGender((Integer) memberAttributeValue);
                }
                break;
            case birth:
                if (memberAttributeValue instanceof Timestamp || memberAttributeValue == null) {
                    setBirth((Timestamp) memberAttributeValue);
                }
                break;
            case area:
                if (memberAttributeValue instanceof Long || memberAttributeValue == null) {
                    setArea((Long) memberAttributeValue);
                }
                break;
            case address:
                if (memberAttributeValue instanceof String || memberAttributeValue == null) {
                    setAddress((String) memberAttributeValue);
                }
                break;
            case zipCode:
                if (memberAttributeValue instanceof String || memberAttributeValue == null) {
                    setZipCode((String) memberAttributeValue);
                }
                break;
            case phone:
                if (memberAttributeValue instanceof String || memberAttributeValue == null) {
                    setPhone((String) memberAttributeValue);
                }
                break;
            case mobile:
                if (memberAttributeValue instanceof String || memberAttributeValue == null) {
                    setMobile((String) memberAttributeValue);
                }
                break;
            case text:
            case select:
                if ((memberAttributeValue instanceof String || memberAttributeValue == null) && propertyIndex != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + propertyIndex;
                        PropertyUtils.setProperty(this, propertyName, memberAttributeValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                break;
            case checkbox:
                if ((memberAttributeValue instanceof Collection || memberAttributeValue == null) && propertyIndex != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + propertyIndex;
                        PropertyUtils.setProperty(this, propertyName, memberAttributeValue != null ? JsonUtils.toJson(memberAttributeValue) : null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                break;
        }
    }

    /**
     * 移除所有会员注册项值
     */
    @Transient
    public void removeAttributeValue() {
        setName(null);
        setGender(null);
        setBirth(null);
        setArea(null);
        setAddress(null);
        setZipCode(null);
        setPhone(null);
        setMobile(null);
        for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
            try {
                PropertyUtils.setProperty(this, propertyName, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }


    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private String     address;
    private BigDecimal amount;
    private String     attributeValue0;
    private String     attributeValue1;
    private String     attributeValue2;
    private String     attributeValue3;
    private String     attributeValue4;
    private String     attributeValue5;
    private String     attributeValue6;
    private String     attributeValue7;
    private String     attributeValue8;
    private String     attributeValue9;
    private BigDecimal balance;
    private Timestamp  birth;
    private String     email;
    private Integer    gender;
    private Boolean    isEnabled;
    private Boolean    isLocked;
    private String     lockKey;
    private Timestamp  lockedDate;
    private Timestamp  loginDate;
    private Integer    loginFailureCount;
    private String     loginIp;
    private String     loginPluginId;
    private String     mobile;
    private String     name;
    private String     nickname;
    private String     openId;
    private String     password;
    private String     phone;
    private Long       point;
    private String     registerIp;
    private Timestamp  safeKeyExpire;
    private String     safeKeyValue;
    private String     username;
    private String     zipCode;
    private Long       area;
    private Long       memberRank;

    public TMember() {}

    public TMember(TMember value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.amount = value.amount;
        this.attributeValue0 = value.attributeValue0;
        this.attributeValue1 = value.attributeValue1;
        this.attributeValue2 = value.attributeValue2;
        this.attributeValue3 = value.attributeValue3;
        this.attributeValue4 = value.attributeValue4;
        this.attributeValue5 = value.attributeValue5;
        this.attributeValue6 = value.attributeValue6;
        this.attributeValue7 = value.attributeValue7;
        this.attributeValue8 = value.attributeValue8;
        this.attributeValue9 = value.attributeValue9;
        this.balance = value.balance;
        this.birth = value.birth;
        this.email = value.email;
        this.gender = value.gender;
        this.isEnabled = value.isEnabled;
        this.isLocked = value.isLocked;
        this.lockKey = value.lockKey;
        this.lockedDate = value.lockedDate;
        this.loginDate = value.loginDate;
        this.loginFailureCount = value.loginFailureCount;
        this.loginIp = value.loginIp;
        this.loginPluginId = value.loginPluginId;
        this.mobile = value.mobile;
        this.name = value.name;
        this.nickname = value.nickname;
        this.openId = value.openId;
        this.password = value.password;
        this.phone = value.phone;
        this.point = value.point;
        this.registerIp = value.registerIp;
        this.safeKeyExpire = value.safeKeyExpire;
        this.safeKeyValue = value.safeKeyValue;
        this.username = value.username;
        this.zipCode = value.zipCode;
        this.area = value.area;
        this.memberRank = value.memberRank;
    }

    public TMember(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     address,
        BigDecimal amount,
        String     attributeValue0,
        String     attributeValue1,
        String     attributeValue2,
        String     attributeValue3,
        String     attributeValue4,
        String     attributeValue5,
        String     attributeValue6,
        String     attributeValue7,
        String     attributeValue8,
        String     attributeValue9,
        BigDecimal balance,
        Timestamp  birth,
        String     email,
        Integer    gender,
        Boolean    isEnabled,
        Boolean    isLocked,
        String     lockKey,
        Timestamp  lockedDate,
        Timestamp  loginDate,
        Integer    loginFailureCount,
        String     loginIp,
        String     loginPluginId,
        String     mobile,
        String     name,
        String     nickname,
        String     openId,
        String     password,
        String     phone,
        Long       point,
        String     registerIp,
        Timestamp  safeKeyExpire,
        String     safeKeyValue,
        String     username,
        String     zipCode,
        Long       area,
        Long       memberRank
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.address = address;
        this.amount = amount;
        this.attributeValue0 = attributeValue0;
        this.attributeValue1 = attributeValue1;
        this.attributeValue2 = attributeValue2;
        this.attributeValue3 = attributeValue3;
        this.attributeValue4 = attributeValue4;
        this.attributeValue5 = attributeValue5;
        this.attributeValue6 = attributeValue6;
        this.attributeValue7 = attributeValue7;
        this.attributeValue8 = attributeValue8;
        this.attributeValue9 = attributeValue9;
        this.balance = balance;
        this.birth = birth;
        this.email = email;
        this.gender = gender;
        this.isEnabled = isEnabled;
        this.isLocked = isLocked;
        this.lockKey = lockKey;
        this.lockedDate = lockedDate;
        this.loginDate = loginDate;
        this.loginFailureCount = loginFailureCount;
        this.loginIp = loginIp;
        this.loginPluginId = loginPluginId;
        this.mobile = mobile;
        this.name = name;
        this.nickname = nickname;
        this.openId = openId;
        this.password = password;
        this.phone = phone;
        this.point = point;
        this.registerIp = registerIp;
        this.safeKeyExpire = safeKeyExpire;
        this.safeKeyValue = safeKeyValue;
        this.username = username;
        this.zipCode = zipCode;
        this.area = area;
        this.memberRank = memberRank;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Length(max = 200)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Length(max = 200)
    public String getAttributeValue0() {
        return this.attributeValue0;
    }

    public void setAttributeValue0(String attributeValue0) {
        this.attributeValue0 = attributeValue0;
    }

    @Length(max = 200)
    public String getAttributeValue1() {
        return this.attributeValue1;
    }

    public void setAttributeValue1(String attributeValue1) {
        this.attributeValue1 = attributeValue1;
    }

    @Length(max = 200)
    public String getAttributeValue2() {
        return this.attributeValue2;
    }

    public void setAttributeValue2(String attributeValue2) {
        this.attributeValue2 = attributeValue2;
    }

    @Length(max = 200)
    public String getAttributeValue3() {
        return this.attributeValue3;
    }

    public void setAttributeValue3(String attributeValue3) {
        this.attributeValue3 = attributeValue3;
    }

    @Length(max = 200)
    public String getAttributeValue4() {
        return this.attributeValue4;
    }

    public void setAttributeValue4(String attributeValue4) {
        this.attributeValue4 = attributeValue4;
    }

    @Length(max = 200)
    public String getAttributeValue5() {
        return this.attributeValue5;
    }

    public void setAttributeValue5(String attributeValue5) {
        this.attributeValue5 = attributeValue5;
    }

    @Length(max = 200)
    public String getAttributeValue6() {
        return this.attributeValue6;
    }

    public void setAttributeValue6(String attributeValue6) {
        this.attributeValue6 = attributeValue6;
    }

    @Length(max = 200)
    public String getAttributeValue7() {
        return this.attributeValue7;
    }

    public void setAttributeValue7(String attributeValue7) {
        this.attributeValue7 = attributeValue7;
    }

    @Length(max = 200)
    public String getAttributeValue8() {
        return this.attributeValue8;
    }

    public void setAttributeValue8(String attributeValue8) {
        this.attributeValue8 = attributeValue8;
    }

    @Length(max = 200)
    public String getAttributeValue9() {
        return this.attributeValue9;
    }

    public void setAttributeValue9(String attributeValue9) {
        this.attributeValue9 = attributeValue9;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Timestamp getBirth() {
        return this.birth;
    }

    public void setBirth(Timestamp birth) {
        this.birth = birth;
    }

    @NotEmpty
    @Email
    @Length(max = 200)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @NotNull
    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Timestamp getLockedDate() {
        return this.lockedDate;
    }

    public void setLockedDate(Timestamp lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Timestamp getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public Integer getLoginFailureCount() {
        return this.loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginPluginId() {
        return this.loginPluginId;
    }

    public void setLoginPluginId(String loginPluginId) {
        this.loginPluginId = loginPluginId;
    }

    @Length(max = 200)
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(max = 200)
    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @NotEmpty(groups = ValidGroup.Save.class)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(max = 200)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getPoint() {
        return this.point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public String getRegisterIp() {
        return this.registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Timestamp getSafeKeyExpire() {
        return this.safeKeyExpire;
    }

    public void setSafeKeyExpire(Timestamp safeKeyExpire) {
        this.safeKeyExpire = safeKeyExpire;
    }

    public String getSafeKeyValue() {
        return this.safeKeyValue;
    }

    public void setSafeKeyValue(String safeKeyValue) {
        this.safeKeyValue = safeKeyValue;
    }

    @NotEmpty(groups = ValidGroup.Save.class)
    @Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(max = 200)
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getArea() {
        return this.area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    @NotNull
    public Long getMemberRank() {
        return this.memberRank;
    }

    public void setMemberRank(Long memberRank) {
        this.memberRank = memberRank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMember (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(amount);
        sb.append(", ").append(attributeValue0);
        sb.append(", ").append(attributeValue1);
        sb.append(", ").append(attributeValue2);
        sb.append(", ").append(attributeValue3);
        sb.append(", ").append(attributeValue4);
        sb.append(", ").append(attributeValue5);
        sb.append(", ").append(attributeValue6);
        sb.append(", ").append(attributeValue7);
        sb.append(", ").append(attributeValue8);
        sb.append(", ").append(attributeValue9);
        sb.append(", ").append(balance);
        sb.append(", ").append(birth);
        sb.append(", ").append(email);
        sb.append(", ").append(gender);
        sb.append(", ").append(isEnabled);
        sb.append(", ").append(isLocked);
        sb.append(", ").append(lockKey);
        sb.append(", ").append(lockedDate);
        sb.append(", ").append(loginDate);
        sb.append(", ").append(loginFailureCount);
        sb.append(", ").append(loginIp);
        sb.append(", ").append(loginPluginId);
        sb.append(", ").append(mobile);
        sb.append(", ").append(name);
        sb.append(", ").append(nickname);
        sb.append(", ").append(openId);
        sb.append(", ").append(password);
        sb.append(", ").append(phone);
        sb.append(", ").append(point);
        sb.append(", ").append(registerIp);
        sb.append(", ").append(safeKeyExpire);
        sb.append(", ").append(safeKeyValue);
        sb.append(", ").append(username);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(area);
        sb.append(", ").append(memberRank);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TMember that = (TMember) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
