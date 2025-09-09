package com.hitech.dms.web.dao.pr.inv.autolist;

import java.util.List;

import com.hitech.dms.web.model.pr.inv.request.PrAutoListFornvoiceRequestModel;
import com.hitech.dms.web.model.pr.inv.response.PrAutoListFornvoiceResponseModel;

public interface PrAutoListFornvoiceDao {
	public List<PrAutoListFornvoiceResponseModel> fetchPurchaseReturnAutoList(String userCode,
			PrAutoListFornvoiceRequestModel requestModel);
}
