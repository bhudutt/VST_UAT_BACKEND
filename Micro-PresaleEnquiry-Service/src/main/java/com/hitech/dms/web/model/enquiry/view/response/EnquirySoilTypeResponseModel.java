package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquirySoilTypeResponseModel {
	private BigInteger enq_soil_type_id;
	private String soil_type;
}
