/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryCustSoilTypeRequestModel {

	private EnquiryCreateRequestModel enquiryHdr;

	private String soilType;

	private Boolean deleteFlag;
}
