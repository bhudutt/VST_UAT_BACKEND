/**
 * 
 */
package com.hitech.dms.web.model.enquiry.model.response;

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
public class ModelVariantListResponseModel {
	private BigInteger variantID;
	private String variantName;
	private String displayValue;
}
