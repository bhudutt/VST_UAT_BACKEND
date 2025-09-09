/**
 * 
 */
package com.hitech.dms.web.model.issue.create.response;

import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

import lombok.Getter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IssueCreateResponseModel {
	private BigInteger issueId;
	private String issueNumber;
	private String msg;
	private Integer statusCode;
}
