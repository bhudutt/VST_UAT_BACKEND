/**
 * 
 */
package com.hitech.dms.web.entity.delivery;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_DC_CANCEL_REQUEST", 
uniqueConstraints = { @UniqueConstraint(columnNames = "dc_cancel_request_id") })
@Data
public class DeliveryChallanCancelRequestEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1765137329304455943L;
	
	@Id
	@Column(name = "dc_cancel_request_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger dcCancelRequestId;
	
//	@JoinColumn(name = "dc_id")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private DeliveryChallanHdrEntity dcHdr;
	@Column(name = "dc_id")
	private BigInteger dcId;
	
	@Column(name = "cancel_request_date")
	private Date cancelRequestDate;
	
	@Column(name = "dc_cancel_date")
	private Date dcCancelDate;
	
	@Column(name = "dc_cancel_reason_id")
	private BigInteger dcCancelReasonId;
	
	@Column(name = "dc_cancel_remark")
	private String dcCancelRemark;
	
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
