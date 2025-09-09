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
@Table(name = "ADM_MENU_ROLE_DTL")
@Data
public class RoleMenuEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_menu_id")
	private Long roleMenuId;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "menu_id")
	private Long menuId;
}
