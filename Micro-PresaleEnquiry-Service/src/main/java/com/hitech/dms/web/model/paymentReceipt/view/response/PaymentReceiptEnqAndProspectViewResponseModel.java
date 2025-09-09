package com.hitech.dms.web.model.paymentReceipt.view.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptEnqAndProspectViewResponseModel {
	private String enquiryNumber;
	private String enquiryStatus;
	private String enquiryDate;
	private BigInteger enqSourceId;
	private String sourceOfEnquiry;
	private String expectedPurchaseDate;
	private String enquiryStage;
	private BigInteger enquiryStageId;
	private BigInteger modelId;
	private String modelName;
	private String modelDesc;
	private BigInteger machineItemId;
	private String itemNo;
	private String mobileNo;
	private String prospectCode;
	private BigInteger customerCategoryId;
	private String prospectCategory;
	private String companyName;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String whatsappNo;
	private String alternateNo;
	private String phoneNumber;
	private String emailid;
	private String address1;
	private String address2;
	private String address3;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private BigInteger pinId;
	private BigInteger cityId;
	private BigInteger tehsilId;
	private BigInteger districtId;
	private BigInteger stateId;
	private BigInteger countryId;
	private String dateOfBirth;
	private String  anniversaryDate;
	private String panNo;
	private String gstin;
	private String customerBalance;

}
