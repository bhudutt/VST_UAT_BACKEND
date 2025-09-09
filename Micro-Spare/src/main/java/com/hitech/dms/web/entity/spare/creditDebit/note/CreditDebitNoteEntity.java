package com.hitech.dms.web.entity.spare.creditDebit.note;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "PA_CREDIT_DEBIT_NOTE")
@Data
public class CreditDebitNoteEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Cr_Dr_Id")
	private BigInteger creditDebitId;
	
	@Column(name="Branch_Id")
	private BigInteger branchId;
	
	@Column(name = "Voucher_Type_Id")
	private Integer voucherTypeId;
	
	@Column(name = "Cr_Dr_No")
	private String creditDebitNo;
	
	@Column(name = "Cr_Dr_Date")
	private Date creditDebitDate;
	
	@Column(name = "PartyTypeId")
	private Integer partyTypeId;
	
	@Column(name = "PartyCodeId")
	private Integer PartyCodeId;
	
	@Column(name = "Cr_Dr_Amt")
	private BigDecimal creditDebitAmt;
	
	@Column(name = "Remark")
	private String remark;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

}
