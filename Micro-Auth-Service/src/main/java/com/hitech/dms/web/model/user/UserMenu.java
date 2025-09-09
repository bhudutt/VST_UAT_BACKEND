package com.hitech.dms.web.model.user;

import java.io.Serializable;
import java.util.List;

public class UserMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6663868157962704929L;

	private Long menuId;
	private String menuCode;
	private String menuName;
	private String jspName;
	private String pageTitle;
	private Character menuStatus;
	private String menuStatusCheck;
	private String index;
	private String menuUrl;
	private Long parentMenuId;
	private String iconName;
	private List<UserMenu> children;
	
	public String getMenuStatusCheck() {
		return menuStatusCheck;
	}
	public void setMenuStatusCheck(String menuStatusCheck) {
		if(menuStatus != null) {
			menuStatusCheck = menuStatus.toString();
		}
		this.menuStatusCheck = menuStatusCheck;
	}
	private String uriName;
	
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getJspName() {
		return jspName;
	}
	public void setJspName(String jspName) {
		this.jspName = jspName;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public Character getMenuStatus() {
		return menuStatus;
	}
	public void setMenuStatus(Character menuStatus) {
		this.menuStatus = menuStatus;
	}
	public String getUriName() {
		return uriName;
	}
	
	
	public void setUriName(String uriName) {
		if(jspName != null && !jspName.equals("")) {
			String[] temp = jspName.split("\\.");
			for(int i=0; i<temp.length; i++) {
				uriName = temp[i];
				break;
			}
		}
		this.uriName = uriName;
	}
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		if (this.menuCode != null) {
			index = this.menuCode.substring(2, this.menuCode.length());
			index = index.replaceFirst("^0+(?!$)", "");
		}
		this.index = index;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public Long getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public List<UserMenu> getChildren() {
		return children;
	}
	public void setChildren(List<UserMenu> children) {
		this.children = children;
	}
}
