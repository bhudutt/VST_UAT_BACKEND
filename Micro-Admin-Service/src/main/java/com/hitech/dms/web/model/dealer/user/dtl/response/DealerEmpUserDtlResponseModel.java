/**
 * 
 */
package com.hitech.dms.web.model.dealer.user.dtl.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerEmpUserDtlResponseModel {
	private BigInteger empId;
	private String empCode;
	private String empName;
	private String dealerName;
	private BigInteger dealerId;
	private Integer profitCenterId;
	private String profitCenterDesc;
	private BigInteger userId;
	private String userCode;
	private String password;
	private String confirmPassword;
	private Boolean isActive;
	private Integer userTypeId;
	private List<DealerEmpUserRoleListResponseModel> roleList;
}
