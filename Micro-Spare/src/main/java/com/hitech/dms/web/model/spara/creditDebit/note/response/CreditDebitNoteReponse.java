package com.hitech.dms.web.model.spara.creditDebit.note.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CreditDebitNoteReponse {
	
	private BigInteger id;
	
	//private BigInteger voucherTypeId;
	
	private String voucherType;
	
	private String creditDebitNo;
	
	@JsonFormat(pattern = "dd/mm/yyyy")
	private String creditDebitDate;
	
	private BigDecimal creditDebitAmt;

	private String address;
	private String tehsilTalukaMandal;
	private String pincode;
	private String partyCode;
	private String state;
	private String cityVillage;
	private String partyName;
	private String district;
	private String postOffice;
	private String country;
	private String partyCategoryName;
	private String panNo;
	private String gstNo;
//	private String address1;
//	private String address2;
//	private String address3;
	private String mobileNo;
	
	
}
