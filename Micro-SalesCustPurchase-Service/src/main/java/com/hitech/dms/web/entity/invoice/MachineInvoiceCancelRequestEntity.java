/**
 * 
 */
package com.hitech.dms.web.entity.invoice;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_INVOICE_CANCEL_REQUEST", uniqueConstraints = {
		@UniqueConstraint(columnNames = "invoice_cancel_request_id") })
@Data
public class MachineInvoiceCancelRequestEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3767342741346382150L;

	@Id
	@Column(name = "invoice_cancel_request_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger invCancelRequestId;

	@Column(name = "invoice_id")
	private BigInteger salesInvoiceHdrId;

	@Column(name = "invoice_request_date")
	private Date cancelRequestDate;

	@Column(name = "invoice_cancel_date")
	private Date invCancelDate;

	@Column(name = "invoice_cancel_reason_id")
	private BigInteger invCancelReasonId;

	@Column(name = "invoice_cancel_remark")
	private String invCancelRemark;

	@Column(name = "cancel_approval_status")
	private String cacnelApprovalStatus;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
}
