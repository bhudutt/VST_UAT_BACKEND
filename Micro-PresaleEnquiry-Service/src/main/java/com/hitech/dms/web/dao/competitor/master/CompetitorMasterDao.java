package com.hitech.dms.web.dao.competitor.master;

import java.util.List;

import org.springframework.mobile.device.Device;
import com.hitech.dms.web.entity.competitor.master.CompetitorMasterEntity;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterBrandListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterBrandRequestModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterListResponseModel;
import com.hitech.dms.web.model.competitor.master.response.CompetitorMasterResponseModel;


public interface CompetitorMasterDao {

	
	public CompetitorMasterResponseModel createCompetitorMaster(String userCode,
			CompetitorMasterEntity competitorMasterEntity, Device device);
	
	public List<CompetitorMasterListResponseModel> fetchCompetitorMasterList(String userCode);
	
	public List<CompetitorMasterBrandListResponseModel> fetchfetchBrandList(String userCode,
			CompetitorMasterBrandRequestModel competitorMasterBrandRequestModel);

	public CompetitorMasterResponseModel changeActiveStatus(String userCode,Integer id, Character isActive);

	
	
}
