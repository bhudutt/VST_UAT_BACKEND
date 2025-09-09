package com.hitech.dms.web.dao.pcforbranchDealer;

import java.util.List;

import com.hitech.dms.web.model.pcforbranchDealer.request.PcForBranchDealerRequestModel;
import com.hitech.dms.web.model.pcforbranchDealer.response.PcForBranchDealerResponseModel;

public interface PcForBranchDealerDao {
	public List<PcForBranchDealerResponseModel> fetchPcForBranchDealerList(String userCode, String isApplicableFor,
			Long dealerOrBranchId, String forSalesFlag);
	public List<PcForBranchDealerResponseModel> fetchPcForBranchDealerList(String userCode,
			PcForBranchDealerRequestModel pcForBranchDealerRequestModel);
}
