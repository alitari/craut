package de.craut.web;

import java.util.Arrays;
import java.util.List;

public class NavElement {

	private String href;
	private String text;
	private String value;
	private boolean selected;

	public NavElement(String href, String text) {
		this(href, text, null, true);
	}

	public NavElement(String href, String text, String value, boolean selected) {
		super();
		this.href = href;
		this.text = text;
		this.value = value;
		this.selected = selected;
	}

	public String getHref() {
		return href;
	}

	public String getText() {
		return text;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static final NavElement PAGE1 = new NavElement("page1", "page1");
	public static final NavElement PAGE2 = new NavElement("page2", "page2");
	public static final NavElement PAGE3 = new NavElement("page3", "page3");
	public static final NavElement PAGE4 = new NavElement("page4", "page4");

	public static final List<NavElement> MENU_PAGE = Arrays.asList(new NavElement[] { PAGE1, PAGE2, PAGE3, PAGE4 });

}
