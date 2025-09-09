/**
 * 
 */
package com.hitech.dms.web.model.customer.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class CustomerDTLByMobileRequestModel {
	private String userCode;
	private String mobileNumber;
	private String isFor;
	private Long dealerID;
}
