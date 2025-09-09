/**
 * 
 */
package com.hitech.dms.web.model.opex.template.upload.response;

import java.math.BigInteger;

import com.hitech.dms.web.entity.opex.StgOpexHdrEntity;

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
public class OpexBudgetUploadResponseModel {
	private BigInteger opexId;
	private String opexNumber;
	private String msg;
	private int statusCode;
	private StgOpexHdrEntity opexHdrEntity;
}
