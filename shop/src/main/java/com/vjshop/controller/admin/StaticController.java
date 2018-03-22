
package com.vjshop.controller.admin;

import java.sql.Timestamp;
import java.util.*;

import javax.annotation.Resource;

import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TArticleCategoryService;
import com.vjshop.service.TArticleService;
import com.vjshop.service.CacheService;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TProductCategoryService;
import com.vjshop.service.StaticService;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 静态化
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminStaticController")
@RequestMapping("/admin/static")
public class StaticController extends BaseController {

	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 首页
		 */
		index,

		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods,

		/**
		 * 其它
		 */
		other
	}

	@Autowired
	private TArticleService tArticleService;
	@Autowired
	private TArticleCategoryService tArticleCategoryService;
	@Autowired
	private TGoodsService tGoodsService;
	@Autowired
	private TProductCategoryService tProductCategoryService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	/**
	 * 生成静态
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", StaticController.GenerateType.values());
		model.addAttribute("defaultBeginDate", DateUtils.addDays(new Date(), -7));
		model.addAttribute("defaultEndDate", new Date());
		model.addAttribute("articleCategoryTree", tArticleCategoryService.findTree());
		model.addAttribute("productCategoryTree", tProductCategoryService.findTree());
		return "/admin/static/generate";
	}

	/**
	 * 生成静态
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(StaticController.GenerateType generateType, Long articleCategoryId, Long productCategoryId, Date beginDate, Date endDate, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		Timestamp begin = null, end = null;
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
			begin = new Timestamp(beginDate.getTime());
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
			end = new Timestamp(endDate.getTime());
		}
		if (first == null || first < 0) {
			first = 0;
			cacheService.clear();
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case index:
			generateCount = staticService.generateIndex();
			break;
		case article:
			List<TArticle> articles = tArticleService.findList(true, articleCategoryId, true, null, begin, end, first, count);
			for (TArticle article : articles) {
				generateCount += staticService.generate(article);
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			List<TGoods> goodsList = tGoodsService.findList(productCategoryId, true, null, beginDate, endDate, first, count);
			for (TGoods goods : goodsList) {
				generateCount += staticService.generate(goods);
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		case other:
			generateCount = staticService.generateOther();
			break;
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		return data;
	}

}