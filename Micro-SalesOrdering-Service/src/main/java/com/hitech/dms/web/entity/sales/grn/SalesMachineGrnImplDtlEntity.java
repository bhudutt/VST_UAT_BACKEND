/**
 * 
 */
package com.hitech.dms.web.entity.sales.grn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_GRN_ITEM_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "grn_item_id") })
@Data
public class SalesMachineGrnImplDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2139740810428242074L;

	@Id
	@Column(name = "grn_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger grnItemDtlId;

	@JoinColumn(name = "grn_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SalesMachineGrnHDREntity salesMachineGrnHDR;
	
	@Column(name = "vin_id")
	private BigInteger vinId;

	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "INVOICE_QTY")
	private Integer invoiceQty;

	@Column(name = "CHASSISNO")
	private String chassisNo;

	@Column(name = "VIN_NO")
	private String vinNo;

	@Column(name = "ENGINENO")
	private String engineNo;

	@Column(name = "UNIT_PRICE")
	private BigDecimal unitPrice;

	@Column(name = "DISCOUNT")
	private BigDecimal discountAmnt;

	@Column(name = "GST_AMOUNT")
	private BigDecimal gstAmnt;

	@Column(name = "ACCESSIBLE_AMT")
	private BigDecimal assessableAmnt;

	@Column(name = "TOTAL_VALUE")
	private BigDecimal totalAmnt;

	@Column(name = "RECEIPT_QTY")
	private Integer receiptQty;

	@Column(name = "REMARKS")
	private String remarks;

}
