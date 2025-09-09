/**
 * 
 */
package com.hitech.dms.web.model.models.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class ModelByPcIdResponseModel {
	private BigInteger machineItemId;
	private BigInteger modelId;
	private String series;
	private String segment;
	private String variant;
	private String model;
	private String item;
	private String itemDesc;
}
