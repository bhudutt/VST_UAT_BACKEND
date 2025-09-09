/**
 * 
 */
package com.hitech.dms.web.model.allot.item.dtl.response;

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
public class ItemDtlForAllotResponseModel {
	private BigInteger machineItemId;
	private String ItemNo;
	private String itemDesc;
	private List<ChassisNoForAllotResponseModel> chassisList;
	private String productGroup;
}
