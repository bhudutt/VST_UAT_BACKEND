/**
 * 
 */
package com.hitech.dms.web.entity.sales.grn;

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
public class SalesMachineItemInvPEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1717111366226919383L;
	
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
}
