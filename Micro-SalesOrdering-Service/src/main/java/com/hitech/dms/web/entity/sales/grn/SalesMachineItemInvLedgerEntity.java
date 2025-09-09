/**
 * 
 */
package com.hitech.dms.web.entity.sales.grn;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_ITEM_INV_LEDGER")
@Data
public class SalesMachineItemInvLedgerEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -4852814106516941633L;
	
	
	//@EmbeddedId
	//private SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "machine_inventory_id")
	private BigInteger itemInventoryId;
	
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	@Column(name = "transaction_no")
	private String transactionNo;
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@Column(name = "transaction_desc")
	private String transactionDesc;
	@Column(name = "inward")
	private Integer inward;
	@Column(name = "outward")
	private Integer outward;
	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;
	@Column(name = "last_modified_on")
	private Date modifiedDate;
	
	@Column(name = "vin_id")
	private BigInteger vinId;

	@Column(name = "grn_id")
	private BigInteger grnId;
	
	@Column(name = "allot_flag")
	private Integer allotFlag;
	
	@Column(name = "allot_qnty")
	private Integer allotQnty;
	
	
	
}
