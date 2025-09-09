/**
 * 
 */
package com.hitech.dms.web.entity.ledger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_INVENTORY_LEDGER", uniqueConstraints = {
		@UniqueConstraint(columnNames = "machine_inventory_id") })
@Data
public class SalesMachineInventoryLedgerEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7073529882167179598L;

	@Id
	@Column(name = "machine_inventory_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger machineInventoryId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "vin_id")
	private BigInteger vinId;

	@Column(name = "grn_id")
	private BigInteger grnId;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "IN_DOC_TYPE")
	private String inDocType;

	@Column(name = "IN_DOC_NUMBER")
	private String inDocNo;

	@Column(name = "IN_DATE")
	private Date inDate;

	@Column(name = "OUT_DOC_TYPE")
	private String outDocType;

	@Column(name = "OUT_DOC_NUMBR")
	private String outDocNo;

	@Column(name = "OUT_DATE")
	private Date outDate;
	
	@Column(name = "purchase_return_flag")
	private boolean purchaseReturnFlag;

	@Column(name = "allot_flag")
	private boolean allotFlag;
	
	@Column(name = "StockType")
	private String stockType;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
}
