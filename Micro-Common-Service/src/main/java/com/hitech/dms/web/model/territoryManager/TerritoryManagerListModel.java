/**
 * 
 */
package com.hitech.dms.web.model.territoryManager;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TerritoryManagerListModel {
	private BigInteger territoryManagerId;
	private String territoryManagerName;
}
