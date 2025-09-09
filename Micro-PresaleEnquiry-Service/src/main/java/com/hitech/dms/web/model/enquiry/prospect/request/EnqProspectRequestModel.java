/**
 * 
 */
package com.hitech.dms.web.model.enquiry.prospect.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandlerForDDMMMYYYY;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnqProspectRequestModel {
	//@JsonDeserialize(using = DateHandlerForDDMMMYYYY.class)
	private String enqOrFollowupdate;
	//@JsonDeserialize(using = DateHandlerForDDMMMYYYY.class)
	private String edd;
}
