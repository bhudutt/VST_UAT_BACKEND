/**
 * 
 */
package com.hitech.dms.web.entity.delivery;

import java.io.Serializable;
import java.math.BigInteger;

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
@Table(name = "SA_MACHINE_DC_CHECKLIST", uniqueConstraints = { @UniqueConstraint(columnNames = "dc_checklist_id") })
@Data
public class DeliveryChallanCheckListEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3207329449121813687L;

	@Id
	@Column(name = "dc_checklist_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger dcCheckListId;

	@JoinColumn(name = "dc_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryChallanHdrEntity dcHdr;

	@Column(name = "dc_checklist_master_id")
	private Integer checkListId;

	@Column(name = "is_delivered")
	private Boolean isDelivered;
}
