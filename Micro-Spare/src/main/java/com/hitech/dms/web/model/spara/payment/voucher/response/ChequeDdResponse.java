package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.util.Date;

import lombok.Data;
@Data
public class ChequeDdResponse {
	
    private String chequeDdNo;
	
	private Date chequeDdDate;
	
	private Integer chequeBankId;
		
	private Date tranDate;
	
	private String chequeBankName;
	

}
