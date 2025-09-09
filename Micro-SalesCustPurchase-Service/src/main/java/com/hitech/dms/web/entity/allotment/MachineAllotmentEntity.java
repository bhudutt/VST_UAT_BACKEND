/**
 * 
 */
package com.hitech.dms.web.entity.allotment;

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
@Table(name = "SA_MACHINE_ALLOTMENT", uniqueConstraints = { @UniqueConstraint(columnNames = "machine_allotment_id") })
@Data
public class MachineAllotmentEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6384832203893667337L;

	@Id
	@Column(name = "machine_allotment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger machineAllotmentId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "allotment_number", updatable = false)
	private String allotNumber;
	
	@Column(name = "allotment_status")
	private String allotStatus;

	@Column(name = "allotment_date")
	private Date allotDate;

	@Column(name = "only_implement_flag")
	private boolean onlyImplementFlag;

	@Column(name = "customer_master_id")
	private BigInteger customerId;

	@Column(name = "enquiry_id")
	private BigInteger enquiryId;

	@Column(name = "de_allot_flag")
	private boolean deAllotFlag;

	@Column(name = "de_allocated_by_id")
	private BigInteger deAllotedById;

	@Column(name = "de_allotment_date")
	private Date deAllotDate;

	@Column(name = "de_allot_reason")
	private String deAllotReason;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;

	@Column(name = "dealer_machine_transfer_id")
	private BigInteger dealerMachineTransferId;
	
	@Column(name = "btocflag")
	private Integer bToCFlag;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machineAllotHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<MachineAllotmentDtlEntity> enqMachineDtlList;
}
