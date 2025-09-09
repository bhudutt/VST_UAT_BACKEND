/**
 * 
 */
package com.hitech.dms.web.entity.ledger;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Embeddable
@Data
public class SalesMachineItemInvLedgerPEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8914250817775722940L;
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	@Column(name = "transaction_no")
	private String transactionNo;
	@Column(name = "transaction_date")
	private Date transactionDate;
}
