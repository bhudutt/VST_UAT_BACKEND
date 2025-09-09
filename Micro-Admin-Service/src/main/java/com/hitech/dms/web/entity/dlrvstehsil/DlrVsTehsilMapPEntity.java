/**
 * 
 */
package com.hitech.dms.web.entity.dlrvstehsil;

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
public class DlrVsTehsilMapPEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -3885606874175538324L;
	@Column(name = "parent_dealer_id")
	private BigInteger dealerId;

	@Column(name = "tehsil_id")
	private BigInteger tehsilId;
}
