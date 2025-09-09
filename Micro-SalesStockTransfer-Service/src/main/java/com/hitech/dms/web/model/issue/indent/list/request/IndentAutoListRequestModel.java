/**
 * 
 */
package com.hitech.dms.web.model.issue.indent.list.request;

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
public class IndentAutoListRequestModel {
	private BigInteger dealerId;
	private Integer branchId;
	private Integer pcId;
	private String searchText;
}
