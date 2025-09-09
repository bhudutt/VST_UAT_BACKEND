/**
 * 
 */
package com.hitech.dms.web.entity.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

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
@Table(name = "SA_MACHINE_INVOICE_ITEM_DTL", uniqueConstraints = {
		@UniqueConstraint(columnNames = "sales_invoice_item_id") })
@Data
public class MachineInvoiceItemDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -3476709743719726685L;
	
	@Id
	@Column(name = "sales_invoice_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger salesInvoiceItemId;
	
	@JoinColumn(name = "sales_invoice_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MachineInvoiceHDREntity machineInvoiceHDR;
	
	@Column(name = "dc_item_id")
	private BigInteger dcItemId;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
	@Column(name = "CHASSISNO")
	private String chassisNo;

	@Column(name = "VIN_NO")
	private String vinNo;

	@Column(name = "ENGINENO")
	private String engineNo;
	
	@Column(name = "qty")
	private Integer qty;

	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "discount")
	private BigDecimal discountAmnt;

	@Column(name = "igst_per")
	private BigDecimal igst_per;

	@Column(name = "igst_amount")
	private BigDecimal igst_amount;

	@Column(name = "cgst_per")
	private BigDecimal cgst_per;

	@Column(name = "cgst_amount")
	private BigDecimal cgst_amount;

	@Column(name = "sgst_per")
	private BigDecimal sgst_per;

	@Column(name = "sgst_amount")
	private BigDecimal sgst_amount;
	
	@Column(name = "total_gst_per")
	private BigDecimal total_gst_per;

	@Column(name = "total_gst_amount")
	private BigDecimal total_gst_amount;

	@Column(name = "assessable_amount")
	private transient BigDecimal assessableAmnt;

	@Column(name = "total_amount")
	private BigDecimal totalAmnt;

	@Column(name = "remarks")
	private transient String remarks;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;
	
	public BigDecimal getDiscountAmnt() {
		return Objects.isNull(discountAmnt) ? (new BigDecimal("0")) : discountAmnt;
	}
}
