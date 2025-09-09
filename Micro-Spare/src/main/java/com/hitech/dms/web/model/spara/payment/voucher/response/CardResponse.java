package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.util.Date;

import lombok.Data;
@Data
public class CardResponse {
	
   private Integer cardTypeId;
	
	private String cardTypeName;
	
	private String cardNo;
	
	private String cardName;
	
	private Date cardDate;
	
	private String tranNo;
	
	private Date tranDate;

}
