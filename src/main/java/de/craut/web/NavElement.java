package de.craut.web;

import java.util.Arrays;
import java.util.List;

public class NavElement {

	private String href;
	private String text;

	private NavElement(String href, String text) {
		super();
		this.href = href;
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public String getText() {
		return text;
	}

	public static final NavElement PAGE1 = new NavElement("page1", "page1");
	public static final NavElement PAGE2 = new NavElement("page2", "page2");
	public static final NavElement PAGE3 = new NavElement("page3", "page3");
	public static final NavElement PAGE4 = new NavElement("page4", "page4");

	public static final NavElement CREATE = new NavElement("create", "New Event");
	public static final NavElement ITEM2 = new NavElement("item2", "item2");
	public static final NavElement ITEM3 = new NavElement("create", "New Event 3");
	public static final NavElement ITEM4 = new NavElement("item4", "item4");

	public static final List<NavElement> MENU_PAGE = Arrays.asList(new NavElement[] { PAGE1, PAGE2, PAGE3, PAGE4 });

	public static final List<NavElement> MENU_MAIN = Arrays.asList(new NavElement[] { CREATE });

}
