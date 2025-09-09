/**
 * 
 */
package com.hitech.dms.web.model.models.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ModelItemListResponseModel {
	private BigInteger machineItemID;
	private String itemNo;
	private String itemDescription;
	private String displayValue;
	private String model;
	private String variant;
	private Character chassisRequired;
}
