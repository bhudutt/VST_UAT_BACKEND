/**
 * 
 */
package com.hitech.dms.web.model.machinepo.dtl.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePODtlRequestModel {
	@JsonProperty(value = "poHdrId", required = true)
	private BigInteger poHdrId;
	private String poNumber;
}
