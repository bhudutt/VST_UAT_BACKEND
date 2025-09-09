/**
 * 
 */
package com.hitech.dms.web.model.indent.create.response;

import java.math.BigInteger;

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
public class IndentCreateResponseModel {
	private BigInteger indentId;
	private String indentNumber;
	private String msg;
	private Integer statusCode;
}
