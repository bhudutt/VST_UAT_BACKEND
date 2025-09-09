/**
 * 
 */
package com.hitech.dms.web.model.dc.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ItemListForDCCreateRequestModel {
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private int qty;
}
