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
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_PURCH_RET_DTL")
@Data
public class SalesMachinePurchaseReturnDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -167219072895635851L;
	
	@Id
	@Column(name = "purchase_return_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnDtlId;

	//@JoinColumn(name = "purchase_return_id" )
	//@ManyToOne(fetch = FetchType.LAZY)
	@Transient
	private SalesMachinePurchaseReturnEntity salesMachinePurchaseReturn;
	
	
	  @Column(name = "purchase_return_id") 
	  private BigInteger purchase_return_id;
	 
	
	@Column(name = "grn_detail_id")
	private BigInteger grnDtlId;
	
	@Column(name = "vin_id")
	private BigInteger vinId;
	
	@Column(name = "vin_no")
	private String vinNo;

	@Column(name = "engine_no")
	private String engineNo;
	
	@Column(name = "chassis_no")
	private String chassisNo;
	
	private transient boolean isDeleted;
	
	@Column(name = "invoice_quantity")
	private Integer invoiceQty;
	
	@Column(name = "return_quantity")
	private Integer receiptQty;
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Column(name = "Discount_amount")
	private BigDecimal discountAmnt;

	@Column(name = "gst_amount")
	private BigDecimal gstAmnt;

	@Column(name = "assessable_amount")
	private BigDecimal assessableAmnt;

	@Column(name = "total_amount")
	private BigDecimal totalAmnt;
	
	@Column(name = "remarks")
	private String remarks;
}
