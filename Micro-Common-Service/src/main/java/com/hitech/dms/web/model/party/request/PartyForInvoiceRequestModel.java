package com.hitech.dms.web.model.party.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyForInvoiceRequestModel extends PartyRequestModel {
	private Integer invTypeId;
	private String searchValue;
}
