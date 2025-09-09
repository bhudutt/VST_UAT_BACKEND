/**
 * 
 */
package com.hitech.dms.web.model.user;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserHoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5548433658131469330L;
	private Long hoUserId;
	private String userType;
	private String pToken;
	private String pName;
	private String pFirstName;
	private String pMiddleName;
	private String pLastName;
	private String pContactNo;
	private String pMail;
	private Integer usrBifurcation;
	private String baseLocation;
	private Long pinId;

	private String panNo;
	private String fatherName;
	private Integer qualification;
	@DateTimeFormat(pattern="dd/MM/YYYY")
	private Date dob;
	@DateTimeFormat(pattern="dd/MM/YYYY")
	private Date doj;
	private String prof_Exp;
	private String remarks;
	private Integer branchId;
	
	private Boolean isActive;
}
