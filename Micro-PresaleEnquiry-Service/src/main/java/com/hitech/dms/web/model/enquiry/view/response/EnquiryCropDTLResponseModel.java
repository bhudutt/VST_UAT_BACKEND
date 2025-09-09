package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryCropDTLResponseModel {
	private BigInteger enq_crop_id;
	private String crop_grown;
}
