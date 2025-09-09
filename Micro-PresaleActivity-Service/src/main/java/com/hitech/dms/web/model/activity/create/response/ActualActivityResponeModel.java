/**
 * 
 */
package com.hitech.dms.web.model.activity.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActualActivityResponeModel {
	private BigInteger activityActualHdrId;
	private String activityActualNo;
	private String msg;
	private Integer statusCode;
}
