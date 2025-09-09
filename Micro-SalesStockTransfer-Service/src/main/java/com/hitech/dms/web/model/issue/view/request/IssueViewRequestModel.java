/**
 * 
 */
package com.hitech.dms.web.model.issue.view.request;

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
public class IssueViewRequestModel {
	private BigInteger issueId;
	private int flag;
}
