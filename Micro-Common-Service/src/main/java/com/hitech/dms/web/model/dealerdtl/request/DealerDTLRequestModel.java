package com.hitech.dms.web.model.dealerdtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerDTLRequestModel {
	private BigInteger dealerId;
	private String isFor;
}
