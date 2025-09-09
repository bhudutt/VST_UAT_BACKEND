package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.util.Date;

import lombok.Data;

@Data
public class ClaimDetailModel {

	//claimNo;
	private Date claimDate;
	private  String claimType;
	String claimStatus;
	private float claimQty;
	

}
