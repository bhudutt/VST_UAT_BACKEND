package com.hitech.dms.web.model.oldchassis;

import lombok.Data;
/**
 * @author Sunil.Singh
 *
 */
@Data
public class OldChassisCustomerDTLByMobileRequestModel {
	
	private String userCode;
	private String mobileNumber;
	private String isFor;
	private Long dealerID;
}
