/**
 * 
 */
package com.hitech.dms.web.model.scheme.type.responseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class SchemeTypeMstResponseModel {
	private Integer schemeTypeId;
	private String schemeTypeCode;
	private String schemeTypeName;
	private String incentiveUnit;
	private String displayName;
}
