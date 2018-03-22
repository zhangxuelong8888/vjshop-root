package com.vjshop;

import java.util.List;

/**
 * @author ck
 * @date 2017/6/14 0014
 */
public class IndexQueryPage {

    /**
     * 总记录数
     */
    private Integer resultSize;

    /**
     * 返回结果集
     */
    private List resultList;


    public Integer getResultSize() {
        return resultSize;
    }

    public void setResultSize(Integer resultSize) {
        this.resultSize = resultSize;
    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }
}
