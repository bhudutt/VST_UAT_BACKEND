/**
 * 
 */
package com.hitech.dms.web.model.machinepo.orderTo.response;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOOrderToResponseModel {
	private Integer poOrderToId;
	private String typeCode;
	private String typeName;
	private String applicableToDealer;
	private String applicableToDistributor;
}
