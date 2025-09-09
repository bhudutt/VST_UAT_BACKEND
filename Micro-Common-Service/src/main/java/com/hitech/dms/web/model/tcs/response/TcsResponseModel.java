/**
 * 
 */
package com.hitech.dms.web.model.tcs.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class TcsResponseModel {
	private BigInteger tcsId;
	private BigDecimal tcsPer;
}
