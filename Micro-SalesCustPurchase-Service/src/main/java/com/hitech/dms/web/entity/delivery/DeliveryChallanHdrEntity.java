/**
 * 
 */
package com.hitech.dms.web.entity.delivery;

import java.io.Serializable;
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
@Table(name = "SA_MACHINE_DC_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "dc_id") })
@Data
public class DeliveryChallanHdrEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -455487858810911749L;

	@Id
	@Column(name = "dc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger dcId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "dc_type_id")
	private Integer dcTypeId;

	@Column(name = "dc_number", updatable = false)
	private String dcNumber;

	@Column(name = "dc_date")
	private Date dcDate;

	@Column(name = "machine_allotment_id")
	private BigInteger machineAllotmentId;

	@Column(name = "enquiry_id")
	private BigInteger enquiryId;

	@Column(name = "dealer_machine_transfer_id")
	private BigInteger dealerMachineTransferId;

	@Column(name = "customer_master_id")
	private BigInteger customerId;

	@Column(name = "insurance_master_id")
	private BigInteger insuranceMasterId;

	@Column(name = "policy_start_date")
	private Date policyStartDate;

	@Column(name = "policy_expiry_date")
	private Date policyEndDate;

	@Column(name = "dc_remarks")
	private String dcRemarks;

	@Column(name = "dc_status")
	private String dcStatus;

	@Column(name = "dc_cancel_flag")
	private boolean dcCancelFlag;

	@Column(name = "dc_cancel_date")
	private Date dcCancelDate;

	@Column(name = "dc_cancel_reason_id")
	private BigInteger dcCancelReasonId;

	@Column(name = "dc_cancel_remark")
	private String dcCancelRemark;

	@Column(name = "dc_cancellation_type")
	private String dcCancellationType;

	@Column(name = "gate_pass_number")
	private String gatePassNumber;

//	@Column(name = "vehicle_registration_number")
//	private String vehicleRegistrationNumber;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dcHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DeliveryChallanItemEntity> dcItemList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dcHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DeliveryChallanDtlEntity> dcDtlList;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dcHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DeliveryChallanCheckListEntity> dcCheckList;

//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dcHdr", cascade = CascadeType.ALL)
//	@Fetch(value = FetchMode.SUBSELECT)
//	private List<DeliveryChallanCancelRequestEntity> dcCancelRequestList;
//
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dcHdr", cascade = CascadeType.ALL)
//	@Fetch(value = FetchMode.SUBSELECT)
//	private List<DeliveryChallanCancelApprovalEntity> dcCancelApprovalList;
}
