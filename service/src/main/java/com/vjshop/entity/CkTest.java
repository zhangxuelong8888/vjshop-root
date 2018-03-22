package com.vjshop.entity;

import com.vjshop.generated.db.tables.pojos.CkTest1;
import com.vjshop.generated.db.tables.pojos.CkTest2;

import java.util.List;

/**
 * @author ck
 * @date 2017/5/4 0004
 */
public class CkTest extends CkTest1 {

    public List<CkTest2> getCkTest2() {
        return ckTest2;
    }

    public void setCkTest2(List<CkTest2> ckTest2) {
        this.ckTest2 = ckTest2;
        this.ckTest2.removeIf(ckTest21 -> ckTest21.getId() == null);
    }

    private List<CkTest2> ckTest2;

}
