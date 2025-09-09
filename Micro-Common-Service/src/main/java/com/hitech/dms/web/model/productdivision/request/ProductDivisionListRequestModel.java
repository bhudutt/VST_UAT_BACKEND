/**
 * 
 */
package com.hitech.dms.web.model.productdivision.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ProductDivisionListRequestModel {
	private BigInteger dealerId;
	private Integer pcId;
	private String isFor;
}
