/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret.inv;

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
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_PURCH_RET_INV_ITEM")
@Data
public class SalesMachinePurchaseReturnInvImplDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8185999775000265305L;

	@Id
	@Column(name = "purchase_ret_inv_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnInvItemId;

	//@JoinColumn(name = "purchase_ret_inv_id")
	//@ManyToOne(fetch = FetchType.LAZY)
	@Transient
	private SalesMachinePurchaseReturnInvEntity salesMachinePurchaseReturnInv;
	
	@Column(name = "purchase_ret_inv_id")
	private BigInteger purchaseRetInvId;

	@Column(name = "purchase_return_item_id")
	private BigInteger purchaseReturnItemId;

	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "VIN_NO")
	private String vinNo;

	@Column(name = "ENGINENO")
	private String engineNo;

	@Column(name = "CHASSISNO")
	private String chassisNo;

	@Column(name = "INVOICE_QTY")
	private Integer invoiceQty;

	@Column(name = "RETURN_QTY")
	private Integer returnQuantity;

	@Column(name = "UNIT_PRICE")
	private BigDecimal unitPrice;

	@Column(name = "DISCOUNT")
	private BigDecimal discountAmnt;

	@Column(name = "IGST_PER")
	private BigDecimal igstPer;

	@Column(name = "IGST_AMOUNT")
	private BigDecimal igstAmount;

	@Column(name = "CGST_PER")
	private BigDecimal cgstPer;

	@Column(name = "CGST_AMOUNT")
	private BigDecimal cgstAmount;

	@Column(name = "SGST_PER")
	private BigDecimal sgstPer;

	@Column(name = "SGST_AMOUNT")
	private BigDecimal sgstAmount;

	@Column(name = "TOTAL_GST_AMOUNT")
	private BigDecimal totalGstAmount;

	@Column(name = "ACCESSIBLE_AMT")
	private BigDecimal assessableAmnt;

	@Column(name = "TOTAL_VALUE")
	private BigDecimal totalAmnt;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;
	
	private transient boolean isDeleted;
}
