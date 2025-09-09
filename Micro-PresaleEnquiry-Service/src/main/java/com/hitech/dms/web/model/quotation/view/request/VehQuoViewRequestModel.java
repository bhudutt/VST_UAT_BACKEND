package com.hitech.dms.web.model.quotation.view.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoViewRequestModel {
	private BigInteger quotationId;
	private BigInteger branchhId;
	private Integer flag;
}
