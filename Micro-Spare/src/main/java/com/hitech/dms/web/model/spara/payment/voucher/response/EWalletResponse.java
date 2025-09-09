package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.util.Date;

import lombok.Data;
@Data
public class EWalletResponse {

   private String eTranNo;
	
	private Date eTranDate;
	
	private String eServProvider;
}
