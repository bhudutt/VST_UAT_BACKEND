/**
 * 
 */
package com.hitech.dms.web.model.dc.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcCreateResponseModel {
	private BigInteger dcId;
	private String dcNumber;
	private String msg;
	private Integer statusCode;
}
