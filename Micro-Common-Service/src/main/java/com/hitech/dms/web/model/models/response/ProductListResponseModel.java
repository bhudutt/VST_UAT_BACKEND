/**
 * 
 */
package com.hitech.dms.web.model.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ProductListResponseModel {
	private Integer valueID;
	private String valueName;
}
