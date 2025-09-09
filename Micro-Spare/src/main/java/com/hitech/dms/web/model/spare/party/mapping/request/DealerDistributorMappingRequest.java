package com.hitech.dms.web.model.spare.party.mapping.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorModel;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorModel;

import lombok.Data;

@Data
public class DealerDistributorMappingRequest {

	private Integer dealerId;
	private BigInteger headerId;
	private List<DealerDistributorModel> dealerDistributorModelList;

}
