/**
 * 
 */
package com.hitech.dms.web.model.aop.template.upload.response;

import java.math.BigInteger;

import com.hitech.dms.web.entity.aop.StgAopHdrEntity;

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
public class AopTargetUploadResponseModel {
	private BigInteger aopId;
	private String aopNumber;
	private String msg;
	private int statusCode;
	private StgAopHdrEntity aopHdrEntity;
}
