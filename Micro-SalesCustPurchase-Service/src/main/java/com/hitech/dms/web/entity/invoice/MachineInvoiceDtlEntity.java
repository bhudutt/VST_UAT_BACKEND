/**
 * 
 */
package com.hitech.dms.web.entity.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

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
@Table(name = "SA_MACHINE_INVOICE_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "sales_invoice_dtl_id") })
@Data
public class MachineInvoiceDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 926870347841281722L;

	@Id
	@Column(name = "sales_invoice_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger salesInvoiceDtlId;

	@JoinColumn(name = "sales_invoice_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MachineInvoiceHDREntity machineInvoiceHDR;

	@Column(name = "dc_id")
	private BigInteger dcId;
	
	@Column(name = "machine_inventory_id")
	private BigInteger machineInventoryId;
	
	private transient Boolean isSelected;
	private transient BigInteger machineItemId;
	private transient BigInteger vinId;

	@Column(name = "quantity")
	private Integer qty;

	@Column(name = "rate")
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
