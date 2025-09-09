package com.hitech.dms.web.model.party.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyListRequestModel extends PartyRequestModel {
	private Integer poOnId;
	private String searchValue;
}
