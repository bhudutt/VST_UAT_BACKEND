package com.hitech.dms.web.dao.pr.inv.search;

import java.util.List;

import com.hitech.dms.web.model.pr.inv.search.request.PrForInvoiceSearchRequestModel;
import com.hitech.dms.web.model.pr.inv.search.response.PrForInvoiceSearchMainResponseModel;
import com.hitech.dms.web.model.pr.inv.search.response.PurchaseReturnNo;

public interface PrForInvoiceSearchDao {
	public PrForInvoiceSearchMainResponseModel searchSalesPurchaseReturnInvList(String userCode,
			PrForInvoiceSearchRequestModel requestModel);

	public List<PurchaseReturnNo> getPurchaseReturnNoList(String searchText, String userCode);
}
