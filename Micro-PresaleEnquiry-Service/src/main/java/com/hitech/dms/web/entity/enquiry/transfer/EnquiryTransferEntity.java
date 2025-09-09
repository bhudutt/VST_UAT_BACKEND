package com.hitech.dms.web.entity.enquiry.transfer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ENQ_TRANSFER_HISTORY")
@Entity
@Data
public class EnquiryTransferEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3707368561668863120L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transfer_history_id")
	private BigInteger transferHistoryId;

	@Column(name = "enquiry_id")
	private BigInteger enquiryHdrId;

	@Column(name = "transfer_from_id")
	private BigInteger transferFromId;

	@Column(name = "transfer_to_id")
	private BigInteger transferToId;

	@Column(name = "transfer_date")
	private Date transferDate;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
}
