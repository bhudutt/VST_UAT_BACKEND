/**
 * 
 */
package com.hitech.dms.web.model.allot.autolist.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineEnqAllotAutoListRequestModel {
	private String searchText;
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private String isFor;
	private String includeInactive;
}
