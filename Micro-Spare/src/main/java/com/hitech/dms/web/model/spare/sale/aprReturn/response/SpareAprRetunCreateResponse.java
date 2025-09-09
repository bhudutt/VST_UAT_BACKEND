package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import lombok.Data;

@Data
public class SpareAprRetunCreateResponse {
	
	private Integer aprReturnHdrId;
	private String aprReturnNumber;
	private String msg;
	private Integer statusCode;

}
