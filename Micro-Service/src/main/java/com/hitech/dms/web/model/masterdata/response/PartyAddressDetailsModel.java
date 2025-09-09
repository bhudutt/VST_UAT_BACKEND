package com.hitech.dms.web.model.masterdata.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PartyAddressDetailsModel {

	private Integer customerId;
	private String customerName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String companyName;
	private String dateOfBirth;
	private String contactTitle;
	private String customerCode;
	private String address_1;
	private String address_2;
	private String address_3;
	private String pinCode;
	private String localityName;
	private String tehsilDesc;
	private String cityDesc;
	private String stateDesc;
	private String countryDesc;
	private Integer cityId;
	private Integer pinId;
	private String mNumber;
	private String fax;
	private String email;
	private String contactPersonName;
	private String gstNo;
	private String customerType;
	private BigDecimal partDiscount = BigDecimal.ZERO;;
	private BigDecimal oilDiscount = BigDecimal.ZERO;;
	private BigDecimal labourDiscount = BigDecimal.ZERO;;
	private Integer stateid;
	private String saleType;
	private String mobileNo;
}
