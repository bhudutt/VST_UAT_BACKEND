package com.hitech.dms.web.model.dealer.user.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerUserRoleRequestModel {
	private BigInteger userRoleId;
	private BigInteger roleId;
	private BigInteger userId;
	private Boolean isActive;

}
