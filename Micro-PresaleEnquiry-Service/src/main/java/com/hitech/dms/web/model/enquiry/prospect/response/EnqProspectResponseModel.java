/**
 * 
 */
package com.hitech.dms.web.model.enquiry.prospect.response;

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
public class EnqProspectResponseModel {
	private BigInteger enqTypeId;
	private String enqType;
}
