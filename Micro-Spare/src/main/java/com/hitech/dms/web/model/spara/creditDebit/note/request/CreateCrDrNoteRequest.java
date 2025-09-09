package com.hitech.dms.web.model.spara.creditDebit.note.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class CreateCrDrNoteRequest {
	
	
	private BigInteger creditDebitId;
	
	private Integer voucherTypeId;
	
	private String creditDebitNo;
	
	private String creditDebitDate;
	
	private Integer partyTypeId;
	
	private Integer partyCodeId;
	
	private BigDecimal creditDebitAmt;
	
	private String remark;
	
	private String voucherType;
	
	private BigInteger branchId;
	
}
