/**
 * 
 */
package com.hitech.dms.web.model.models.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachineItemResponseModel {
	private BigInteger itemId;
	private String itemNumber;
	private String itemDescription;
	private String displayValue;
	private String variant;
	private BigDecimal mrp;
	private BigDecimal ndp;
	private BigDecimal gstPer;
	private BigDecimal gstAmnt;
}
