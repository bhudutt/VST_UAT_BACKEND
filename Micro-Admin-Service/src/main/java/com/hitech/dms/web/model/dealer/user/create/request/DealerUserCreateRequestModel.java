package com.hitech.dms.web.model.dealer.user.create.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerUserCreateRequestModel {
	
	private BigInteger dealrId;
	private BigInteger userId;
	private Integer userTypeId;
	private String username;
	private BigInteger dlrEmpId;
	private String password;
	private String confirmPassword;
	private Boolean isActive;
	
	private List<DealerUserRoleRequestModel> roleList;

}
