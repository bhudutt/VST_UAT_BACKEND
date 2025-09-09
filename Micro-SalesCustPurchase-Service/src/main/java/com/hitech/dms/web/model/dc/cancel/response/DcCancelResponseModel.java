/**
 * 
 */
package com.hitech.dms.web.model.dc.cancel.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcCancelResponseModel {
	private BigInteger dcId;
	private String dcNumber;
	private String msg;
	private Integer statusCode;
}
