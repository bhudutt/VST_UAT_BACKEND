/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret;

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

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_PURCH_RET_ITEM_DTL")
@Data
public class SalesMachinePurchaseReturnImplDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2379661950610102088L;

	@Id
	@Column(name = "purchase_return_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnItemId;

	@JoinColumn(name = "purchase_return_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SalesMachinePurchaseReturnEntity salesMachinePurchaseReturn;

	@Column(name = "grn_item_id")
	private BigInteger grnItemDtlId;

	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "CHASSISNO")
	private String chassisNo;

	@Column(name = "VIN_NO")
	private String vinNo;

	@Column(name = "ENGINENO")
	private String engineNo;
	
	private transient boolean isDeleted;

	@Column(name = "INVOICE_QTY")
	private Integer invoiceQty;

	@Column(name = "RETURN_QTY")
	private Integer receiptQty;

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

	@Column(name = "REMARKS")
	private String remarks;
}
