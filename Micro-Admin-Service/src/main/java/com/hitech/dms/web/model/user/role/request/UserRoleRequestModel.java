/**
 * 
 */
package com.hitech.dms.web.model.user.role.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class UserRoleRequestModel {
	@JsonProperty(value = "userId", required = true)
	private BigInteger userId;
	@JsonProperty(value = "isFor", required = true)
	private String isFor;
}
