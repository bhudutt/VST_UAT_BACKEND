package com.hitech.dms.web.dao.common.search;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.common.search.CommonFilterDealerModel;
import com.hitech.dms.web.model.common.search.CommonFilterDealerResponse;
import com.hitech.dms.web.model.common.search.CommonFilterFinalResponse;
import com.hitech.dms.web.model.common.search.CommonFilterHoModel;
import com.hitech.dms.web.model.common.search.CommonFilterModel;
import com.hitech.dms.web.model.common.search.CommonFilterPcListRes;
import com.hitech.dms.web.model.common.search.CommonFilterStateModel;
import com.hitech.dms.web.model.common.search.CommonFilterZoneModel;
import com.hitech.dms.web.model.common.search.CommonTerritoryModel;
import com.hitech.dms.web.model.common.search.DistrictResponse;

public interface CommonFilterPcDao {

	
	public CommonFilterPcListRes  getPcListCommon(CommonFilterModel request,String userCode);
	public List<CommonFilterHoModel> getCommonHoList(CommonFilterModel request,String userCode);
	public List<CommonFilterZoneModel> getCommonZoneList(CommonFilterModel request,String userCode);
	public List<CommonFilterStateModel> getCommonStateList(CommonFilterModel request,String userCode);
	public List<CommonTerritoryModel> getCommonTerritoryList(CommonFilterModel request,String userCode);
	public CommonFilterDealerResponse getCommonDealerList(CommonFilterModel request,String userCode);
	public CommonFilterFinalResponse getCommonFilterAllResponse(BigInteger pcId,String userCode);
	public List<DistrictResponse> getCommonDistrictByStateIdResponse(BigInteger stateId, String userCode);
	
	
	
}
