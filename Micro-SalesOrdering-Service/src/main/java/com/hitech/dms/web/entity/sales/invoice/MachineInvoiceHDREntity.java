/**
 * 
 */
package com.hitech.dms.web.entity.sales.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_INVOICE_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "sales_invoice_id") })
@Data
public class MachineInvoiceHDREntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4532159965219235286L;

	@Id
	@Column(name = "sales_invoice_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger salesInvoiceHdrId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "invoice_type_id")
	private Integer invoiceTypeId;

	@Column(name = "invoice_number", updatable = false)
	private String invoiceNumber;

	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "invoice_status")
	private String invoiceStatus;

	@Column(name = "customer_master_id")
	private BigInteger customerId;

	@Column(name = "to_dealer_id")
	private BigInteger toDealerId;

	@Column(name = "to_po_hdr_id")
	private BigInteger toPoHdrId;

	@Column(name = "insurance_charges")
	private BigDecimal insuranceCharges;

	@Column(name = "rto_charges")
	private BigDecimal rtoCharges;

	@Column(name = "other_charges")
	private BigDecimal otherCharges;

	@Column(name = "total_basic_amount")
	private BigDecimal totalBasicAmnt;

	@Column(name = "total_gst_amount")
	private BigDecimal totalGstAmnt;

	@Column(name = "total_invoice_amount")
	private BigDecimal totalInvoiceAmnt;

	@Column(name = "financer_id")
	private BigInteger financerId;

	@Column(name = "invoice_cancel_flag")
	private boolean invoiceCancelFlag;

	@Column(name = "invoice_cancel_date")
	private Date invoiceCancelDate;

	@Column(name = "invoice_cancel_reason_id")
	private Integer invoiceCancelReasonId;

	@Column(name = "invoice_cancel_remark")
	private String invoiceCancelRemark;

	@Column(name = "invoice_cancellation_type")
	private String invoiceCancelType;

	@Column(name = "insurance_master_id")
	private BigInteger insuranceMasterId;

	@Column(name = "policy_start_date")
	private Date policyStartDate;

	@Column(name = "policy_expiry_date")
	private Date policyExpiryDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machineInvoiceHDR", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<MachineInvoiceDtlEntity> machineInvoiceDtlList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machineInvoiceHDR", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<MachineInvoiceItemDtlEntity> machineInvoiceItemDtlList;
}
