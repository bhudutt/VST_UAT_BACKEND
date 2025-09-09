package com.hitech.dms.web.spare.party.model.mapping.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorModel;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorModel;

import lombok.Data;

@Data
public class DealerDistributorMappingRequest {

	private Integer dealerId;
	private Integer headerId;
	private List<DealerDistributorModel> dealerDistributorModelList;

}
