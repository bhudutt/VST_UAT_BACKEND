package com.hitech.dms.web.dao.inv.po.search;

import com.hitech.dms.web.model.inv.search.request.InvoiceSearchRequestModel;
import com.hitech.dms.web.model.inv.search.response.InvoiceSearchMainResponseModel;

public interface InvoiceSearchDao {
	public InvoiceSearchMainResponseModel searchInvoiceList(String userCode, InvoiceSearchRequestModel requestModel);
}
