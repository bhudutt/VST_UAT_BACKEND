/**
 * 
 */
package com.hitech.dms.web.model.salesman.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class SalesmanListModel {
	private BigInteger salesmanID;
	private String salesmanName;
}
