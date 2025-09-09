/**
 * 
 */
package com.hitech.dms.web.model.issue.indent.dtl.request;

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
public class IndentDtlRequestModel {
	private BigInteger indentId;
	private String indentNumber;
	private int flag;
}
