/**
 * 
 */
package com.hitech.dms.web.model.lookup.response;

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
public class SysLookupResponseModel {
	private BigInteger valueId;
	private String valueCode;
	private String displayValue;
}
