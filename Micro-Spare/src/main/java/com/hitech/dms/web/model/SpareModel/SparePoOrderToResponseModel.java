/**
 * 
 */
package com.hitech.dms.web.model.SpareModel;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoOrderToResponseModel {
	private Integer poOrderToId;
	private String typeCode;
	private String typeName;
	private String applicableToDealer;
	private String applicableToDistributor;
}
