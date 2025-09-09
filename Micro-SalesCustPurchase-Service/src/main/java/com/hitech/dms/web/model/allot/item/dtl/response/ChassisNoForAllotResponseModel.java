/**
 * 
 */
package com.hitech.dms.web.model.allot.item.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ChassisNoForAllotResponseModel {
	private BigInteger vinId;
	private String chassisNo;
}
