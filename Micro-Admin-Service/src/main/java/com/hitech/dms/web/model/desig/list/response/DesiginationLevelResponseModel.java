/**
 * 
 */
package com.hitech.dms.web.model.desig.list.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DesiginationLevelResponseModel {
	private Integer desigLevelId;
	private String desigLevelCode;
	private String desigLevelDesc;
	private Integer departmentId;
}
