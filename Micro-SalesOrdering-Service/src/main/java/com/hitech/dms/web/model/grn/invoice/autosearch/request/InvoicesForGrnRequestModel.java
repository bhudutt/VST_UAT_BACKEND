package com.hitech.dms.web.model.grn.invoice.autosearch.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoicesForGrnRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private Integer grnTypeId;
	private String grnNumber;
	private String includeInactive;
}
