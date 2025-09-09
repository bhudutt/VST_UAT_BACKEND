/**
 * 
 */
package com.hitech.dms.web.model.quotation.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoViewResponseModel {
	private BigInteger quotationId;
	private String quotationNumber;
	private String quotationDate;
	private String customerMobileNumber;
	private String customerCode;
	private String prospectType;
	private String customerCompanyName;
	private String customerContactTitle;
	private String firstName;
	private String middleName;
	private String lastName;
	private String whatsappNumber;
	private String alternateNumber;
	private String stdPhoneNumber;
	private String emailId;
	private String address1;
	private String address2;
	private String address3;
	private String district;
	private String tehsil;
	private String village;
	private String pinCode;
	private String state;
	private String country;
	private String dateOfBirth;
	private String anniversaryDate;
	private String panNumber;
	private String gstNumber;
	private BigDecimal totalBasicValue;
	private BigDecimal totalDiscount;
	private BigDecimal totalTaxableValue;
	private BigDecimal totalGstAmount;
	private BigDecimal totalCharges;
	private BigDecimal totalAmount;
	private BigDecimal insurance;
	private BigDecimal rto;
	private BigDecimal hsrp;
	private BigDecimal charges;
	private List<VehQuoDTLViewResponseModel> vehQuoDTLList;
	private List<VehQuoImplementViewResponseModel> vehQuoImplementList;
	
}
