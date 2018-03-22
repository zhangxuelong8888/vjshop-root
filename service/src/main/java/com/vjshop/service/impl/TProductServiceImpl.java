
package com.vjshop.service.impl;

import com.vjshop.dao.TProductDao;
import com.vjshop.dao.TStockLogDao;
import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TStockLog;
import com.vjshop.generated.db.tables.records.TProductRecord;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 商品
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tProductServiceImpl")
public class TProductServiceImpl extends TBaseServiceImpl<TProductRecord,TProduct ,Long> implements TProductService {

	@Autowired
	private TGoodsService tGoodsService;
	@Autowired
	private TProductDao tProductDao;
	@Autowired
	private TStockLogDao tStockLogDao;

	@Transactional
	public void addStock(TProduct product, int amount, TStockLog.Type type, TAdmin operator, String memo) {
		Assert.notNull(product);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		Timestamp now = new Timestamp(System.currentTimeMillis());

		Assert.notNull(product.getStock());
		Assert.state(product.getStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setStock(product.getStock() + amount);
		product.setModifyDate(now);
		this.tProductDao.update(product);

		TGoods goods = product.getGoodsVO();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(TGoods.GenerateMethod.eager.ordinal());
			} else {
				goods.setGenerateMethod(TGoods.GenerateMethod.lazy.ordinal());
			}
		}

		TStockLog stockLog = new TStockLog();
		stockLog.setType(type.ordinal());
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator.getUsername());
		stockLog.setMemo(memo);
		stockLog.setProductVO(product);
		stockLog.setProduct(product.getId());
		stockLog.setCreateDate(now);
		stockLog.setModifyDate(now);
		stockLog.setVersion(0L);
		this.tStockLogDao.insertAndFetch(stockLog);
	}

	@Override
	@Transactional
	public void addAllocatedStock(TProduct product, int amount) {
		Assert.notNull(product);

		if (amount == 0) {
			return;
		}

		Assert.notNull(product.getAllocatedStock());
		Assert.state(product.getAllocatedStock() + amount >= 0);

		Timestamp now = new Timestamp(System.currentTimeMillis());

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setAllocatedStock(product.getAllocatedStock() + amount);
		product.setModifyDate(now);

		TGoods goods = product.getGoodsVO();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(TGoods.GenerateMethod.eager.ordinal());
			} else {
				goods.setGenerateMethod(TGoods.GenerateMethod.lazy.ordinal());
			}
			goods.setModifyDate(now);
			this.tGoodsService.update(goods);
		}
		this.tProductDao.update(product);
	}


	@Transactional(readOnly = true)
	public void filter(List<TProduct> products) {
		CollectionUtils.filter(products, new Predicate() {
			public boolean evaluate(Object object) {
				TProduct product = (TProduct) object;
				return product != null && product.getStock() != null;
			}
		});
	}


	/****************************************************************************************************************/
	public TProduct findDetailById(Long productId){
		if(productId == null) return null;
		TProduct tProduct = this.find(productId);
		TGoods tGoods = this.tGoodsService.findDetailById(tProduct.getGoods());
		tProduct.setGoodsVO(tGoods);
		return tProduct;
	}

	/**
	 * 下拉框模糊搜索product
	 * @param keyword
	 * @param count
	 * @return
	 */
	public List<TProduct> search(String keyword , Integer count){
		List<TProduct> tProducts = this.tProductDao.search(keyword,count);

		return tProducts;
	}

	/**
	 * 编辑product
	 */
	/*public void updates(TProduct product) {


		tProductDao.update(product);

	}*/
}