/**
 * 
 */
package com.hitech.dms.web.model.dc.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcViewRequestModel {
	private BigInteger dcId;
	private String dcNumber;
	private String isFor;
	private int flag;
}
