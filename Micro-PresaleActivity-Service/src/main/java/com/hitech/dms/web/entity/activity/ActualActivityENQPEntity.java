/**
 * 
 */
package com.hitech.dms.web.entity.activity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Embeddable
@Data
public class ActualActivityENQPEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5715265428882525353L;

	@Column(name = "activity_actual_hdr_id")
	private BigInteger activityActualHdrId;

	@Column(name = "enquiry_id")
	private BigInteger enquiryId;
}
