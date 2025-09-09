/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@NamedNativeQueries({ @NamedNativeQuery(name = "fetchADMMenudetails", query = "Select * from ADM_MENU_MST admMenu ") })
@Table(name = "ADM_MENU_MST")
@Data
public class MenuDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long menuId;
	
	@Column(name = "menutype_id")
	private transient int menuTypeId;
	
	@Column(name = "menu_code")
	private String menuCode;
	
	@Column(name = "menu_name")
	private String menuName;
	
	@Column(name = "iconName")
	private String iconName;
	
	@Column(name = "jspname")
	private String jspName;
	
	@Column(name = "page_title")
	private String pageTitle;
	
	@Column(name = "MenuUrl")
	private String menuUrl;
	
	@Column(name = "order_no")
	private int orderNo;
	
	@Column(name = "parent_menuid")
	private int parentMenuId;
	
	@Column(name = "is_active")
	@Type(type = "yes_no")
	private Boolean active;
	
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;
	
	private transient Character activeCheck;
	
	transient private String path;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = result * prime + (this.menuId != null ? this.menuId.hashCode() : 0);
		result = result * prime + (this.pageTitle != null ? this.pageTitle.hashCode() : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		MenuDetailsEntity detailsEntity = (MenuDetailsEntity) obj;
		return (this.getMenuId() == detailsEntity.getMenuId() && this.getMenuCode() == detailsEntity.getMenuCode() && this.getPageTitle().equals(detailsEntity.getPageTitle()));
	}
	
	@Override
	public String toString() {
		return "Menu Detsils : "+menuCode+" : "+jspName;
	}
}
