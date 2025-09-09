package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.util.Date;

import lombok.Data;
@Data
public class RtgsNeftImpsResponse {
	
    private String rtgsTranNo;
	
	private Date rtgsTranDate;

	private Integer rtgsTranBankId;
	
	private String rtgsTranBankName;

}
