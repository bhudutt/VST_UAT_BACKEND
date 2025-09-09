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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.hitech.dms.web.entity.allotment.MachineAllotmentEntity;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_DC_DTL", 
uniqueConstraints = { @UniqueConstraint(columnNames = "dc_machine_dtl_id") })
@Data
public class DeliveryChallanDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4934095396050956656L;
	
	@Id
	@Column(name = "dc_machine_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger machineDcDtlId;

	@JoinColumn(name = "dc_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryChallanHdrEntity dcHdr;

	@Column(name = "machine_inventory_id")
	private BigInteger machineInventoryId;
	
	@Column(name = "productType")
	private String productType;
	@Transient
	private BigInteger itemId;
	
	@Column(name = "quantity")
	private Integer qty;
	
	
}
