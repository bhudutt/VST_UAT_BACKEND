package com.hitech.dms.web.model.user;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class RoleDtlModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5702044340946244719L;
	private BigInteger roleId;
	private String roleCode;
	private String roleName;
	private Boolean isActive;
}
