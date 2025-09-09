/**
 * 
 */
package com.hitech.dms.web.model.pcforbranchDealer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class PcForBranchDealerResponseModel {
	private Integer pcId;
	private String pcCode;
	private String pcName;
}
