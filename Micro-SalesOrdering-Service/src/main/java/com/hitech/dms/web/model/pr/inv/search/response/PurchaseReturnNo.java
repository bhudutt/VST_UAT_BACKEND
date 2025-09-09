package com.hitech.dms.web.model.pr.inv.search.response;

import java.math.BigInteger;

import lombok.Data;
@Data
public class PurchaseReturnNo {

	private  BigInteger purchaseReturnId;
	private String purchaseReturnName;
	private String purchaseReturnNumber;
}
