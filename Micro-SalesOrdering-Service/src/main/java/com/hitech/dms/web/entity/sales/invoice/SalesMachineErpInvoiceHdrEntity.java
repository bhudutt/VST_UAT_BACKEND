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
@Table(name = "SA_MACHINE_ERP_INVOICE_HDR", uniqueConstraints = {
		@UniqueConstraint(columnNames = "erp_invoice_hdr_id") })
@Data
public class SalesMachineErpInvoiceHdrEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 927569995381191400L;

	@Id
	@Column(name = "erp_invoice_hdr_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger erpInvoiceHdrId;

	@Column(name = "erp_dealer_id")
	private BigInteger erpDealerId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "invoice_number", updatable = false)
	private String invoiceNumber;

	@Column(name = "invoice_date")
	private Date invoiceDate;

	@Column(name = "bill_to")
	private String billTo;

	@Column(name = "ship_to")
	private String shipTo;

	@Column(name = "additional_amount")
	private BigDecimal additionalAmnt;

	@Column(name = "additional_cgst_amount")
	private BigDecimal additionalCgstAmnt;

	@Column(name = "additional_igst_amount")
	private BigDecimal additionalIgstAmnt;

	@Column(name = "additional_sgst_amount")
	private BigDecimal additionalSgstAmnt;

	@Column(name = "total_additional_amount")
	private BigDecimal totalAdditionalAmnt;

	@Column(name = "invoice_total_value")
	private BigDecimal invoiceTotalValue;

	@Column(name = "lr_no")
	private String lrNo;

	@Column(name = "lr_date")
	private Date lrDate;

	@Column(name = "transporter_name")
	private String transporterName;

	@Column(name = "grn_done_flag")
	private boolean grnDoneFlag;

	@Column(name = "pdi_done_flag")
	private boolean pdiDoneFlag;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_on", updatable = false)
	private Date createdDate;

	@Column(name = "modified_by")
	private BigInteger modifiedBy;

	@Column(name = "modified_on")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "salesMachineErpInvoiceHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SalesMachineErpInvoiceDtlEntity> salesMachineErpInvoiceDtlList;
}
