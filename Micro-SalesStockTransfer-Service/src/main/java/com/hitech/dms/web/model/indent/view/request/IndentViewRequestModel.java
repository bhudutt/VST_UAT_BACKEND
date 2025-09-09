/**
 * 
 */
package com.hitech.dms.web.model.indent.view.request;

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
public class IndentViewRequestModel {
	private BigInteger indentId;
	private int flag;
}
