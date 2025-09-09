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
@Table(name = "SA_MACHINE_ITEM_INV_LEDGER")
@Data
public class SalesMachineItemInvLedgerEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -4852814106516941633L;
	@EmbeddedId
	private SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP;
	@Column(name = "transaction_desc")
	private String transactionDesc;
	@Column(name = "inward")
	private Integer inward;
	@Column(name = "outward")
	private Integer outward;
	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;
	@Column(name = "last_modified_on")
	private Date modifiedDate;
}
