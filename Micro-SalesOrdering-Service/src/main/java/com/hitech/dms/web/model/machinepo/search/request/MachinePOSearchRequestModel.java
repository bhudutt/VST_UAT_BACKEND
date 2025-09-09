/**
 * 
 */
package com.hitech.dms.web.model.machinepo.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String poNumber;
	private String poStatus;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private Integer poTypeId;
	private Integer poPlantId;
	private BigInteger poToDealerId;
	//@JsonDeserialize(using = DateHandler.class)
	private String fromDate;
	//@JsonDeserialize(using = DateHandler.class)
	private String toDate;
	private String includeInActive;
	private int page;
	private int size;
}
