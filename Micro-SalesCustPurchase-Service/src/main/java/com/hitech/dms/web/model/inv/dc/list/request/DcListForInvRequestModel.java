/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.list.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcListForInvRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger customerId;
	private Integer invoiceTypeId;
	private String mobileNumber;
	private String isFor;
	private int flag;
}
