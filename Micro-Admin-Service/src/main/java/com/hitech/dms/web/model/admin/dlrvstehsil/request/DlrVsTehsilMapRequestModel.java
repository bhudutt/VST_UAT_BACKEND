/**
 * 
 */
package com.hitech.dms.web.model.admin.dlrvstehsil.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DlrVsTehsilMapRequestModel {
	private BigInteger dealerId;
	private List<DlrVsTehsilListRequestModel> tehsilList;
}
