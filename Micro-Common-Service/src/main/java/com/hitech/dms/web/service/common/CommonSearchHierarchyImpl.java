package com.hitech.dms.web.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.common.CommonSearchHierarchy;
import com.hitech.dms.web.model.common.response.CommonHoDetailResponse;
import com.hitech.dms.web.model.common.response.HoModel;
import com.hitech.dms.web.model.common.response.OrgHierBranchList;
import com.hitech.dms.web.model.common.response.OrgHierDealerList;
import com.hitech.dms.web.model.common.response.OrgHierLevelModel;
import com.hitech.dms.web.model.common.response.ProfitCenterModel;

@Service
public class CommonSearchHierarchyImpl  implements CommonSearchHierarchyService{

	@Autowired
	CommonSearchHierarchy commonDao;
	
	@Override
	public CommonHoDetailResponse fetchCommonHoOrgDetailService(Integer flag, String userCode) {
		
		CommonHoDetailResponse mainList = new CommonHoDetailResponse();
		List<ProfitCenterModel>PcList1= new ArrayList<>();
		List<HoModel> hoModelList= new ArrayList<>();
		List<OrgHierLevelModel> orgHierLevelList= new ArrayList<>();
		List<OrgHierDealerList> orgHierDealerList= new ArrayList<>();
		 List<OrgHierBranchList> orgHierBranchList= new ArrayList<>();

		CommonHoDetailResponse pcList = new CommonHoDetailResponse();
		CommonHoDetailResponse hoList = new CommonHoDetailResponse();
		CommonHoDetailResponse orgHierList = new CommonHoDetailResponse();
		CommonHoDetailResponse dealerList = new CommonHoDetailResponse();
		CommonHoDetailResponse branchList = new CommonHoDetailResponse();
		return commonDao.fetchCommonHoOrgDetail(flag, userCode);
				
			
			
		
	}

}
