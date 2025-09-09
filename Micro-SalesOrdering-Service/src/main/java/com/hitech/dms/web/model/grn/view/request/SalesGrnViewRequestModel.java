/**
 * 
 */
package com.hitech.dms.web.model.grn.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class SalesGrnViewRequestModel {
	private BigInteger grnId;
	private BigInteger dealerId;
	private int flag;
}
