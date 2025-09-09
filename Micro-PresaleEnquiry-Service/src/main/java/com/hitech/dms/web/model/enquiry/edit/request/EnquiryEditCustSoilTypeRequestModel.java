/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditCustSoilTypeRequestModel {

	private EnquiryEditRequestModel enquiryHdr;

	private String soilType;

	private Boolean deleteFlag;
}
