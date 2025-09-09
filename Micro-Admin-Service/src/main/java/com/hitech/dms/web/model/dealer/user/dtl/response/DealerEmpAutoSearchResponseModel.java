/**
 * 
 */
package com.hitech.dms.web.model.dealer.user.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
public interface DealerEmpAutoSearchResponseModel {
	public BigInteger getEmpId();
	public String getEmpCode();
	public String getEmpName();
	public String getDisplayValue();
}
