/**
 * 
 */
package com.hitech.dms.web.entity.allotment;

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
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_ALLOTMENT_DTL")
@Data
public class MachineAllotmentDtlEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2197305788038816764L;
	@Id
	@Column(name = "machine_allotment_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger machineAllotmentDtlId;

	@JoinColumn(name = "machine_allotment_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MachineAllotmentEntity machineAllotHdr;

	@Column(name = "machine_inventory_id")
	private BigInteger machineInventoryId;
	
	private transient Boolean isAlloted;
	
	@Column(name = "product_type")
	private String productGroup;
	
	@Column(name = "quantity")
	private BigInteger allotQnty;
	
	
}
