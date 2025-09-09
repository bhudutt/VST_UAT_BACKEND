package com.hitech.dms.web.model.machineItem.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ItemListResponseModel {
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDescription;
	private String displayValue;
}
