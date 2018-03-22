
package com.vjshop;

import java.awt.Color;
import java.awt.Font;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * 验证码图片生成
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("captchaEngine")
public class CaptchaEngine extends ImageCaptchaEngine implements ServletContextAware, InitializingBean {

	/** ServletContext */
	private ServletContext servletContext;

	/** 图片宽度 */
	@Value("${captcha.imageWidth}")
	private int imageWidth;

	/** 图片高度 */
	@Value("${captcha.imageHeight}")
	private int imageHeight;

	/** 最小字体大小 */
	@Value("${captcha.minFontSize}")
	private int minFontSize;

	/** 最大字体大小 */
	@Value("${captcha.maxFontSize}")
	private int maxFontSize;

	/** 最小字符长度 */
	@Value("${captcha.minWordLength}")
	private int minWordLength;

	/** 最大字符长度 */
	@Value("${captcha.maxWordLength}")
	private int maxWordLength;

	/** 随机字符 */
	@Value("${captcha.charString}")
	private String charString;

	/** 随机背景图片路径 */
	@Value("${captcha.background_image_path}")
	private String backgroundImagePath;

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 初始化
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.state(imageWidth > 0);
		Assert.state(imageHeight > 0);
		Assert.state(minFontSize > 0);
		Assert.state(maxFontSize > 0);
		Assert.state(minWordLength > 0);
		Assert.state(maxWordLength > 0);
		Assert.hasText(charString);

		Font[] fonts = new Font[] { new Font("Arial", Font.BOLD, maxFontSize), new Font("Bell", Font.BOLD, maxFontSize), new Font("Credit", Font.BOLD, maxFontSize), new Font("Impact", Font.BOLD, maxFontSize) };
		FontGenerator fontGenerator = new RandomFontGenerator(minFontSize, maxFontSize, fonts);
		BackgroundGenerator backgroundGenerator = StringUtils.isNotEmpty(backgroundImagePath) ? new FileReaderRandomBackgroundGenerator(imageWidth, imageHeight, servletContext.getRealPath(backgroundImagePath)) : new FunkyBackgroundGenerator(imageWidth, imageHeight);
		TextPaster textPaster = new RandomTextPaster(minWordLength, maxWordLength, Color.WHITE);
		CaptchaFactory[] captchaFactories = new CaptchaFactory[] { new GimpyFactory(new RandomWordGenerator(charString), new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster)) };
		super.setFactories(captchaFactories);
	}

}