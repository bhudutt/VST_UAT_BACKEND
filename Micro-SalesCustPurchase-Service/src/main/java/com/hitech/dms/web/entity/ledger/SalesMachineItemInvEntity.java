/**
 * 
 */
package com.hitech.dms.web.entity.ledger;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_ITEM_INV")
@Data
public class SalesMachineItemInvEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 5013336636641626781L;

	@EmbeddedId
	private SalesMachineItemInvPEntity salesMachineItemInvP;

	@Column(name = "stock_qty")
	private Integer stockQty;
	
	@Column(name = "blocked_qty")
	private Integer blockedQty;
	
	@Column(name = "net_stock_qty")
	private Integer netStockQty;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_on", updatable = false)
	private Date createdDate;

	@Column(name = "modified_by")
	private BigInteger modifiedBy;

	@Column(name = "modified_on")
	private Date modifiedDate;
}
