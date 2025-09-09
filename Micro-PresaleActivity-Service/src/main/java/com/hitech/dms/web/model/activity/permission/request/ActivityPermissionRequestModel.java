/**
 * 
 */
package com.hitech.dms.web.model.activity.permission.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPermissionRequestModel {
	private BigInteger id;
	private String isFor;
}
