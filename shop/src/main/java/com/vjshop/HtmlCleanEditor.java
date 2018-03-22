
package com.vjshop;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Editor - HTML清理
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class HtmlCleanEditor extends PropertyEditorSupport {

	/** 默认白名单 */
	public static final Whitelist DEFAULT_WHITELIST = Whitelist.none();

	/** 宽松白名单 */
	public static final Whitelist RELAXED_WHITELIST;

	static {
		Whitelist relaxedWhitelist = new Whitelist();
		relaxedWhitelist.addTags("a", "abbr", "acronym", "address", "applet", "area", "article", "aside", "audio", "b", "base", "basefont", "bdi", "bdo", "big", "blockquote", "body", "br", "button", "canvas", "caption", "center", "cite", "code", "col", "colgroup", "command", "datalist", "dd",
				"del", "details", "dir", "div", "dfn", "dialog", "dl", "dt", "em", "embed", "fieldset", "figcaption", "figure", "font", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hr", "html", "i", "iframe", "img", "input", "ins", "isindex", "kbd",
				"keygen", "label", "legend", "li", "link", "map", "mark", "menu", "menuitem", "meta", "meter", "nav", "noframes", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "section", "select", "small",
				"source", "span", "strike", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "time", "title", "tr", "track", "tt", "u", "ul", "var", "video", "wbr", "xmp");
		relaxedWhitelist.addAttributes("a", "charset", "coords", "download", "href", "hreflang", "media", "name", "rel", "rev", "shape", "target", "type");
		relaxedWhitelist.addAttributes("code", "object", "applet", "align", "alt", "archive", "codebase", "height", "hspace", "name", "vspace", "width");
		relaxedWhitelist.addAttributes("area", "alt", "coords", "href", "nohref", "shape", "target");
		relaxedWhitelist.addAttributes("audio", "autoplay", "controls", "loop", "muted", "preload", "src");
		relaxedWhitelist.addAttributes("base", "href", "target");
		relaxedWhitelist.addAttributes("basefont", "color", "face", "size");
		relaxedWhitelist.addAttributes("bdi", "dir");
		relaxedWhitelist.addAttributes("bdo", "dir");
		relaxedWhitelist.addAttributes("blockquote", "cite");
		relaxedWhitelist.addAttributes("body", "alink", "background", "bgcolor", "link", "text", "vlink");
		relaxedWhitelist.addAttributes("button", "autofocus", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "name", "type", "value");
		relaxedWhitelist.addAttributes("canvas", "height", "width");
		relaxedWhitelist.addAttributes("caption", "align");
		relaxedWhitelist.addAttributes("col", "align", "char", "charoff", "span", "valign", "width");
		relaxedWhitelist.addAttributes("colgroup", "align", "char", "charoff", "span", "valign", "width");
		relaxedWhitelist.addAttributes("command", "checked", "disabled", "icon", "label", "radiogroup", "type");
		relaxedWhitelist.addAttributes("del", "cite", "datetime");
		relaxedWhitelist.addAttributes("details", "open");
		relaxedWhitelist.addAttributes("dir", "compact");
		relaxedWhitelist.addAttributes("div", "align");
		relaxedWhitelist.addAttributes("dialog", "open");
		relaxedWhitelist.addAttributes("embed", "height", "src", "type", "width");
		relaxedWhitelist.addAttributes("fieldset", "disabled", "form", "name");
		relaxedWhitelist.addAttributes("font", "color", "face", "size");
		relaxedWhitelist.addAttributes("form", "accept", "accept-charset", "action", "autocomplete", "enctype", "method", "name", "novalidate", "target");
		relaxedWhitelist.addAttributes("frame", "frameborder", "longdesc", "marginheight", "marginwidth", "name", "noresize", "scrolling", "src");
		relaxedWhitelist.addAttributes("frameset", "cols", "rows");
		relaxedWhitelist.addAttributes("h1", "align");
		relaxedWhitelist.addAttributes("h2", "align");
		relaxedWhitelist.addAttributes("h3", "align");
		relaxedWhitelist.addAttributes("h4", "align");
		relaxedWhitelist.addAttributes("h5", "align");
		relaxedWhitelist.addAttributes("h6", "align");
		relaxedWhitelist.addAttributes("head", "profile");
		relaxedWhitelist.addAttributes("hr", "align", "noshade", "size", "width");
		relaxedWhitelist.addAttributes("html", "manifest", "xmlns");
		relaxedWhitelist.addAttributes("iframe", "align", "frameborder", "height", "longdesc", "marginheight", "marginwidth", "name", "sandbox", "scrolling", "seamless", "src", "srcdoc", "width");
		relaxedWhitelist.addAttributes("img", "alt", "src", "align", "border", "height", "hspace", "ismap", "longdesc", "usemap", "vspace", "width");
		relaxedWhitelist.addAttributes("input", "accept", "align", "alt", "autocomplete", "autofocus", "checked", "disabled", "form", "formaction", "formenctype", "formmethod", "formnovalidate", "formtarget", "height", "list", "max", "maxlength", "min", "multiple", "name", "pattern", "placeholder",
				"readonly", "required", "size", "src", "step", "type", "value", "width");
		relaxedWhitelist.addAttributes("ins", "cite", "datetime");
		relaxedWhitelist.addAttributes("keygen", "autofocus", "challenge", "disabled", "form", "keytype", "name");
		relaxedWhitelist.addAttributes("label", "for", "form");
		relaxedWhitelist.addAttributes("legend", "align");
		relaxedWhitelist.addAttributes("li", "type", "value");
		relaxedWhitelist.addAttributes("link", "charset", "href", "hreflang", "media", "rel", "rev", "sizes", "target", "type");
		relaxedWhitelist.addAttributes("map", "id", "name");
		relaxedWhitelist.addAttributes("menu", "label", "type");
		relaxedWhitelist.addAttributes("menuitem", "checked", "default", "disabled", "icon", "open", "label", "radiogroup", "type");
		relaxedWhitelist.addAttributes("meta", "content", "http-equiv", "name", "scheme");
		relaxedWhitelist.addAttributes("meter", "form", "high", "low", "max", "min", "optimum", "value");
		relaxedWhitelist.addAttributes("object", "align", "archive", "border", "classid", "codebase", "codetype", "data", "declare", "form", "height", "hspace", "name", "standby", "type", "usemap", "vspace", "width");
		relaxedWhitelist.addAttributes("ol", "compact", "reversed", "start", "type");
		relaxedWhitelist.addAttributes("optgroup", "label", "disabled");
		relaxedWhitelist.addAttributes("option", "disabled", "label", "selected", "value");
		relaxedWhitelist.addAttributes("output", "for", "form", "name");
		relaxedWhitelist.addAttributes("p", "align");
		relaxedWhitelist.addAttributes("param", "name", "type", "value", "valuetype");
		relaxedWhitelist.addAttributes("pre", "width");
		relaxedWhitelist.addAttributes("progress", "max", "value");
		relaxedWhitelist.addAttributes("q", "cite");
		relaxedWhitelist.addAttributes("section", "cite");
		relaxedWhitelist.addAttributes("select", "autofocus", "disabled", "form", "multiple", "name", "required", "size");
		relaxedWhitelist.addAttributes("source", "media", "src", "type");
		relaxedWhitelist.addAttributes("style", "type", "media");
		relaxedWhitelist.addAttributes("table", "align", "bgcolor", "border", "cellpadding", "cellspacing", "frame", "rules", "summary", "width");
		relaxedWhitelist.addAttributes("tbody", "align", "char", "charoff", "valign");
		relaxedWhitelist.addAttributes("td", "abbr", "align", "axis", "bgcolor", "char", "charoff", "colspan", "headers", "height", "nowrap", "rowspan", "scope", "valign", "width");
		relaxedWhitelist.addAttributes("textarea", "autofocus", "cols", "disabled", "form", "maxlength", "name", "placeholder", "readonly", "required", "rows", "wrap");
		relaxedWhitelist.addAttributes("tfoot", "align", "char", "charoff", "valign");
		relaxedWhitelist.addAttributes("th", "abbr", "align", "axis", "bgcolor", "char", "charoff", "colspan", "headers", "height", "nowrap", "rowspan", "scope", "valign", "width");
		relaxedWhitelist.addAttributes("thead", "align", "char", "charoff", "valign");
		relaxedWhitelist.addAttributes("time", "datetime", "pubdate");
		relaxedWhitelist.addAttributes("title", "dir", "lang", "xml:lang");
		relaxedWhitelist.addAttributes("tr", "align", "bgcolor", "char", "charoff", "valign");
		relaxedWhitelist.addAttributes("track", "default", "kind", "label", "src", "srclang");
		relaxedWhitelist.addAttributes("ul", "compact", "type");
		relaxedWhitelist.addAttributes("video", "autoplay", "controls", "height", "loop", "muted", "poster", "preload", "src", "width");
		relaxedWhitelist.addAttributes(":all", "accesskey", "class", "contenteditable", "contextmenu", "data-*", "dir", "draggable", "dropzone", "hidden", "id", "lang", "spellcheck", "style", "tabindex", "title", "translate");
		relaxedWhitelist.addProtocols("a", "href", "http", "https", "ftp", "mailto");
		relaxedWhitelist.addProtocols("area", "href", "http", "https");
		relaxedWhitelist.addProtocols("base", "href", "http", "https");
		relaxedWhitelist.addProtocols("link", "href", "http", "https");
		relaxedWhitelist.addProtocols("audio", "src", "http", "https");
		relaxedWhitelist.addProtocols("embed", "src", "http", "https");
		relaxedWhitelist.addProtocols("frame", "src", "http", "https");
		relaxedWhitelist.addProtocols("iframe", "src", "http", "https");
		relaxedWhitelist.addProtocols("img", "src", "http", "https");
		relaxedWhitelist.addProtocols("input", "src", "http", "https");
		relaxedWhitelist.addProtocols("source", "src", "http", "https");
		relaxedWhitelist.addProtocols("track", "src", "http", "https");
		relaxedWhitelist.addProtocols("video", "src", "http", "https");
		relaxedWhitelist.addProtocols("body", "background", "http", "https");
		RELAXED_WHITELIST = relaxedWhitelist;
	}

	/** 是否移除两端空白 */
	private boolean trim;

	/** 是否将空转换为null */
	private boolean emptyAsNull;

	/** 白名单 */
	private Whitelist whitelist = DEFAULT_WHITELIST;

	/**
	 * 构造方法
	 * 
	 * @param trim
	 *            是否移除两端空白
	 * @param emptyAsNull
	 *            是否将空转换为null
	 */
	public HtmlCleanEditor(boolean trim, boolean emptyAsNull) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
	}

	/**
	 * 构造方法
	 * 
	 * @param trim
	 *            是否移除两端空白
	 * @param emptyAsNull
	 *            是否将空转换为null
	 * @param whitelist
	 *            白名单
	 */
	public HtmlCleanEditor(boolean trim, boolean emptyAsNull, Whitelist whitelist) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
		this.whitelist = whitelist;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : StringUtils.EMPTY;
	}

	/**
	 * 设置内容
	 * 
	 * @param text
	 *            内容
	 */
	@Override
	public void setAsText(String text) {
		if (text != null) {
			String value = Jsoup.clean(text, whitelist);
			value = trim ? value.trim() : value;
			if (emptyAsNull && StringUtils.isEmpty(text)) {
				value = null;
			}
			setValue(value);
		} else {
			setValue(null);
		}
	}

}