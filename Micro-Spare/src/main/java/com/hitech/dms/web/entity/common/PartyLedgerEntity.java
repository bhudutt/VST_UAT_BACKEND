package com.hitech.dms.web.entity.common;

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

@Data
@Entity
@Table(name = "PA_PARTY_LEDGER")
public class PartyLedgerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private BigInteger id;
	
	@Column(name="party_id")
	private BigInteger partyId;

	@Column(name="transaction_name")
	private String transactionName;

	@Column(name="transaction_id")
	private BigInteger transactionId;

	@Column(name="transaction_date")
	private Date transactionDate;

	@Column(name="transaction_amount")
	private BigDecimal transactionAmount;

	@Column(name="transaction_type")
	private String transactionType;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private String createdBy;

	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private String updatedBy;
}
