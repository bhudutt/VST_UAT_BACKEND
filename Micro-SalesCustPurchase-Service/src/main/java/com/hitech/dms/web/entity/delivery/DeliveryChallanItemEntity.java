/**
 * 
 */
package com.hitech.dms.web.entity.delivery;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
@Table(name = "SA_MACHINE_DC_ITEM", 
uniqueConstraints = { @UniqueConstraint(columnNames = "dc_item_id") })
@Data
public class DeliveryChallanItemEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6372799322926944255L;
	
	@Id
	@Column(name = "dc_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger dcItemId;
	
	@JoinColumn(name = "dc_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryChallanHdrEntity dcHdr;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
	@Column(name = "quantity")
	private Integer qty;
}
