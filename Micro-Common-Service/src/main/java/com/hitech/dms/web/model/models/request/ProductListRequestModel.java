/**
 * 
 */
package com.hitech.dms.web.model.models.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ProductListRequestModel {
	private String productLevelName;
	private String searchParentText;
	private Long pcId;
}
