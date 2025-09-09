package com.hitech.dms.web.dao.advancetrackingreport;

import java.util.List;

import com.hitech.dms.web.model.advancereport.AdvanceReportSearchListResultResponseModel;
import com.hitech.dms.web.model.advancereport.AdvanceTrackingReportRequestModel;
import com.hitech.dms.web.model.advancereport.FinancierResponseListModel;

public interface AdvanceTrackingReportDao {

	AdvanceReportSearchListResultResponseModel Search(String userCode, AdvanceTrackingReportRequestModel requestModel);

	List<FinancierResponseListModel> fetchFinancierList(String userCode, Object objects);

}
