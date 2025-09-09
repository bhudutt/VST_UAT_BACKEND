/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.dtl.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcDtlForInvResponseModel {
	
	private BigInteger dcId;
	private List<DcMachDtlForInvResponseModel> dcMachineList;
	private List<DcItemDtlForInvResponseModel> dcItemList;
}
