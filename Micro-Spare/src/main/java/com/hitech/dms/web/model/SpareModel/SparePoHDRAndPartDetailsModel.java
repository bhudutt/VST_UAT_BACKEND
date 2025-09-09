/**
 * 
 */
package com.hitech.dms.web.model.SpareModel;

import java.util.List;

import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.partSearchDetailsResponse;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoHDRAndPartDetailsModel {
	List<SparePoHdrDetailsResponse> hdrDetailsResponse ;
	List<partSerachDetailsByPohdrIdResponseModel> partDetailsResponse ;
}
