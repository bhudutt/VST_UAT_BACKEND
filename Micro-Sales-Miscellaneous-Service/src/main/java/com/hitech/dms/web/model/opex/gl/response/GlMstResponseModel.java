/**
 * 
 */
package com.hitech.dms.web.model.opex.gl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class GlMstResponseModel {
	private BigInteger glId;
	private Integer pcId;
	private String glHeaderCode;
	private String glHeaderName;
	private boolean isActive;
}
