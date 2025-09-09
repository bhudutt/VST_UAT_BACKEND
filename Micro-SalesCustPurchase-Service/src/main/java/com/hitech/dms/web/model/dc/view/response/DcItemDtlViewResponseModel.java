/**
 * 
 */
package com.hitech.dms.web.model.dc.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcItemDtlViewResponseModel {
	private BigInteger dcItemId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private int qty;
}
