
package com.vjshop.controller.admin;

import com.vjshop.Pageable;
import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TStockLog;
import com.vjshop.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 库存
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminStockController")
@RequestMapping("/admin/stock")
public class StockController extends BaseController {

	@Autowired
	private TStockLogService tStockLogService;
	@Autowired
	private TProductService tProductService;
	@Autowired
	private TGoodsService tGoodsService;
	@Autowired
	private TAdminService tAdminService;

	/**
	 * 商品选择
	 */
	@RequestMapping(value = "/product_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> productSelect(@RequestParam("q") String keyword, @RequestParam("limit") Integer count) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}
		List<TProduct> products = this.tProductService.search( keyword,  count);
		for (TProduct product : products) {
			product.setGoodsVO(this.tGoodsService.findDetailById(product.getGoods()));
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("stock", product.getStock());
			item.put("allocatedStock", product.getAllocatedStock());
			item.put("specifications", product.getSpecifications());
			data.add(item);
		}
		return data;
	}

	/**
	 * 入库
	 */
	@RequestMapping(value = "/stock_in", method = RequestMethod.GET)
	public String stockIn(Long productId, ModelMap model) {
		model.addAttribute("product", this.tProductService.findDetailById(productId));
		return "/admin/stock/stock_in";
	}

	/**
	 * 入库
	 */
	@RequestMapping(value = "/stock_in", method = RequestMethod.POST)
	public String stockIn(Long productId, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		TProduct product = this.tProductService.findDetailById(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}
		TAdmin admin = this.tAdminService.getCurrent();
		this.tProductService.addStock(product, quantity, TStockLog.Type.stockIn, admin, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 出库
	 */
	@RequestMapping(value = "/stock_out", method = RequestMethod.GET)
	public String stockOut(Long productId, ModelMap model) {
		model.addAttribute("product", tProductService.findDetailById(productId));
		return "/admin/stock/stock_out";
	}

	/**
	 * 出库
	 */
	@RequestMapping(value = "/stock_out", method = RequestMethod.POST)
	public String stockOut(Long productId, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		TProduct product = this.tProductService.findDetailById(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}
		if (product.getStock() - quantity < 0) {
			return ERROR_VIEW;
		}
		TAdmin admin = this.tAdminService.getCurrent();
		this.tProductService.addStock(product, -quantity, TStockLog.Type.stockOut, admin, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 记录
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Pageable pageable, ModelMap model) {
		model.addAttribute("types", TStockLog.Type.values());
		model.addAttribute("page", this.tStockLogService.findPage(pageable));

		return "/admin/stock/log";
	}

}