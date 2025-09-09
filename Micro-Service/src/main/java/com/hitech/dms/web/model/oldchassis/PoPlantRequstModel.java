/**
 * 
 */
package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PoPlantRequstModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String includeInActive;
}
