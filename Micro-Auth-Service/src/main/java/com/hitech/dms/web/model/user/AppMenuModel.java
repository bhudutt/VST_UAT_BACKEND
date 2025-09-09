/**
 * 
 */
package com.hitech.dms.web.model.user;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppMenuModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2042677071248185998L;
	@JsonIgnore
	private Long id;
	@JsonIgnore
	private String menuCode;
	private String displayName;
	@JsonIgnore
	private String pageTitle;
	@JsonIgnore
	private Boolean menuStatus;
	@JsonIgnore
	private Integer menuOrder;
	@JsonIgnore
	private Long parentId;
	private String iconName;
	private String route;
	private boolean disabled = false;
	private String badge;
	private String badgeBg;
	public List<AppMenuModel> children;

	public void addChildrenItem(AppMenuModel childrenItem) {
		if (!this.children.contains(childrenItem))
			this.children.add(childrenItem);
	}
}
