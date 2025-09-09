/**
 * 
 */
package com.hitech.dms.web.entity.sales.grn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_GRN_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "grn_detail_id") })
@Data
public class SalesMachineGrnDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6087468692161921637L;

	@Id
	@Column(name = "grn_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger grnDtlId;

	@JoinColumn(name = "grn_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SalesMachineGrnHDREntity salesMachineGrnHDR;

	@Column(name = "vin_id")
	private BigInteger vinId;
	private transient BigInteger machineItemId;
	
	@Column(name = "chassis_no")
	private String chassisNo;

	@Column(name = "vin_no")
	private String vinNo;

	@Column(name = "engine_no")
	private String engineNo;

	@Column(name = "invoice_quantity")
	private Integer invoiceQty;

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

	@Column(name = "receipt_quantity")
	private Integer receiptQty;
	
	@Column(name = "RETURN_QTY")
	private Integer returnQty;

	@Column(name = "remarks")
	private String remarks;
	
	@Transient
	private String plantCode;
}
