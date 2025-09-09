package com.hitech.dms.web.dao.salesman;

import java.util.List;

import com.hitech.dms.web.model.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.salesman.response.SalesmanListModel;

public interface SalesmanDao {
	List<SalesmanListModel> fetchSalesmanList(String userCode, String dealerOrBranch, Long dealerOrBranchID);
	public List<SalesmanListModel> fetchSalesmanList(SalesmanListFormModel salesmanListFormModel);
}
