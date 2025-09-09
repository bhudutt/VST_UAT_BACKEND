/**
 * 
 */
package com.hitech.dms.web.entity.sales.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
@Table(name = "SA_MACHINE_ERP_INVOICE_DTL")
@Data
public class SalesMachineErpInvoiceDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 523722994688589375L;

	@Id
	@Column(name = "erp_invoice_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger erpInvoiceDtlId;

	@JoinColumn(name = "erp_invoice_hdr_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SalesMachineErpInvoiceHdrEntity salesMachineErpInvoiceHdr;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;

	@Column(name = "chassis_no")
	private String chassisNo;

//	@Column(name = "VIN_NO")
//	private String vinNo;

	@Column(name = "engine_no")
	private String engineNo;

	@Column(name = "invoice_quantity")
	private Integer invoiceQty;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "gst_amount")
	private BigDecimal gstAmnt;
	
	@Column(name = "total_discount")
	private BigDecimal totalAmnt;
	
	@Column(name = "assessable_amount")
	private BigDecimal assessableAmnt;
	
	@Column(name = "total_value")
	private BigDecimal totalValue;
	
	@Column(name = "pdi_done_flag")
	private boolean pdiDone;
	
	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_on", updatable = false)
	private Date createdDate;

	@Column(name = "modified_by")
	private BigInteger modifiedBy;

	@Column(name = "modified_on")
	private Date modifiedDate;
}
