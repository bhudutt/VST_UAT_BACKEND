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
@Table(name = "SA_MACHINE_PURCH_RET_INV_DTL")
@Data
public class SalesMachinePurchaseReturnInvDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 5278007114062422367L;
	@Id
	@Column(name = "purchase_ret_inv_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnInvDtlId;

	//@JoinColumn(name = "purchase_ret_inv_id")
	//@ManyToOne(fetch = FetchType.LAZY)
	@Transient
	private SalesMachinePurchaseReturnInvEntity salesMachinePurchaseReturnInv;
	
	@Column(name = "purchase_ret_inv_id")
	private BigInteger purchaseRetInvId;

	@Column(name = "purchase_return_detail_id")
	private BigInteger purchaseReturnDtlId;

	@Column(name = "vin_id")
	private BigInteger vinId;

	@Column(name = "vin_no")
	private String vinNo;

	@Column(name = "engine_no")
	private String engineNo;

	@Column(name = "chassis_no")
	private String chassisNo;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "Discount_amount")
	private BigDecimal discountAmnt;

	@Column(name = "igst_per")
	private BigDecimal igstPer;

	@Column(name = "igst_amount")
	private BigDecimal igstAmount;

	@Column(name = "cgst_per")
	private BigDecimal cgstPer;

	@Column(name = "cgst_amount")
	private BigDecimal cgstAmount;

	@Column(name = "sgst_per")
	private BigDecimal sgstPer;

	@Column(name = "sgst_amount")
	private BigDecimal sgstAmount;

	@Column(name = "total_gst_amount")
	private BigDecimal totalGstAmount;;

	@Column(name = "assessable_amount")
	private BigDecimal assessableAmnt;

	@Column(name = "total_amount")
	private BigDecimal totalAmnt;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;
	
	private transient boolean isDeleted;
}
