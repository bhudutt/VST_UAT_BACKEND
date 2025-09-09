package com.hitech.dms.web.model.machineItem.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class ItemListRequestModel {
	private Integer pcId;
	private BigInteger modelId;
	private String productGroup;
	private String searchValue;
}
