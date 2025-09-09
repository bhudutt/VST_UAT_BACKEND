/**
 * 
 */
package com.hitech.dms.web.model.inv.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

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
public class InvoiceSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dcNumber;
	private String invoiceNumber;
	private String poNumber;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private String invoiceStatus;
	//@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	//@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private String fromDate1;
	private String toDate1;
	private int page;
	private int size;
}
