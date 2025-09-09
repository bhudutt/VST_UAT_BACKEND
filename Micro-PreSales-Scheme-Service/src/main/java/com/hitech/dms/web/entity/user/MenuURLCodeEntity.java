/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name="ADM_MENU_URL_MST")
@Data
public class MenuURLCodeEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9034530822581778058L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="menu_Url_id")
	private Integer menuUrlId;
	@Column(name="MenuCode")
	private String menuCode;
	@Column(name="MenuUrl")
	private String menuUrl;
}
