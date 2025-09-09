package com.hitech.dms.web.dao.inv.autolist;

import java.util.List;

import com.hitech.dms.web.model.inv.autolist.request.PoInvoiceAutoListRequestModel;
import com.hitech.dms.web.model.inv.autolist.response.PoInvoiceAutoListResponseModel;

public interface PoInvoiceAutoListDao {
	public List<PoInvoiceAutoListResponseModel> fetchPoListForInvoice(String userCode,
			PoInvoiceAutoListRequestModel requestModel);
}
