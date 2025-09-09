/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.admin.role.search.resquest;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleSearchRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String roleCode;
	private String roleName;
	private String applicableTo;
	private Character isActive;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private BigInteger orgHierId;
	private Integer page;
	private Integer size;
	private String includeInactive;

}
