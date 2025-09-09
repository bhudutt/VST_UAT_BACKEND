/**
 * 
 */
package com.hitech.dms.web.model.allot.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineAllotCreateResponseModel {
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String msg;
	private Integer statusCode;
}
