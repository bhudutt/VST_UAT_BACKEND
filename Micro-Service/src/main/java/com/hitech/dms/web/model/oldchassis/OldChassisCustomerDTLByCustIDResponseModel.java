package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author Sunil.Singh
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OldChassisCustomerDTLByCustIDResponseModel {

	private BigInteger customerId;
	private String customerCode;
	private BigInteger customerCategoryID;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String alternateNumber;
	private String watsupNumber;
	private String emailId;
	private String address1;
	private String address2;
	private String address3;
	private BigInteger districtId;
	private String districtDesc;
	private BigInteger tehsilId;
	private String tehsilDesc;
	private BigInteger pinID;
	private String pinCode;
	private BigInteger cityID;
	private String cityDesc;
	private BigInteger countryID;
	private String countryDesc;
	private BigInteger stateID;
	private String stateDesc;
	private String panNo;
	private String aadharcardNo;
}
