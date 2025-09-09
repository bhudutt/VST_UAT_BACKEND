package com.hitech.dms.web.model.dealermaster.create.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DealerListModel {
        
	    private BigInteger id;
	    private String action;
	    private String dealerCode;
	    private String dealerName;
	//    private String contactPerson;
	    private String address1;
	    private String address2;
	    private String district;
	    private String tehsil;
	    private String stateCode;
	    private String city;
	    private String pinCode;
	    private String mobileNumber;
	//    private String telephoneNumber;
	    private String gstNumber;
	    private String panNumber;
	    private String companyCode;
	    private String country;
	    private String mailId;
	    
	    private String bankName;
	    private String bankBranchName;
	    private String ifscCode;
	    private String micrCode;
	    private String TINNo;
	    private String CINNo;
	//    private String ERPVehPriceListCode;
	//   private String ERPPartsPriceListCode;
	    private String SalesOrg;
	//    private String ActivationDate;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private String createdDate;
	    private String  dealerStatus;
	    
}
