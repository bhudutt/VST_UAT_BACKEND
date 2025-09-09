/**
 * 
 */
package com.hitech.dms.web.model.allot.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineAllotViewRequestModel {
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private int flag;
}
