
package com.vjshop.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vjshop.LuceneManager;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TArticleService;
import com.vjshop.service.TGoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 索引
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminIndexController")
@RequestMapping("/admin/index")
public class IndexController extends BaseController {

	@Autowired
	private TArticleService articleService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private LuceneManager luceneManager;

	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods
	}

	/**
	 * 生成索引
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		model.addAttribute("generateTypes", IndexController.GenerateType.values());
		return "/admin/index/generate";
	}

	/**
	 * 生成索引
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> generate(IndexController.GenerateType generateType, Boolean isPurge, Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		boolean isCreate = false;
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case article:
			if (first == 0 && isPurge != null && isPurge) {
				luceneManager.delete(TArticle.class);
				isCreate = true;
			}
			List<TArticle> articles = articleService.findList(first, count, null, null);
			for (TArticle article : articles) {
				luceneManager.index(article, isCreate);
				generateCount++;
			}
			luceneManager.close();
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			if (first == 0 && isPurge != null && isPurge) {
				luceneManager.delete(TGoods.class);
				isCreate = true;
			}
			List<TGoods> goodsList = goodsService.findList(first, count, null, null);
			for (TGoods goods : goodsList) {
				luceneManager.index(goods, isCreate);
				generateCount++;
			}
			luceneManager.close();
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
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