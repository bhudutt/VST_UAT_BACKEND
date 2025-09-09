package com.hitech.dms.web.model.spara.payment.voucher.request;

import java.math.BigInteger;

import lombok.Data;
@Data
public class PVpartyCodeRequestModel {
	
    private BigInteger branchId;
	
	private String searchText;
	
	private BigInteger partyTypeId;
	
	private BigInteger dealerId;
	
	private BigInteger paymentVoucherId;

}
