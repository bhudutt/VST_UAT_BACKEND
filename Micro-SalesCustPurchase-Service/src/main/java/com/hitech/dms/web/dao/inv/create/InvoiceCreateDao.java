package com.hitech.dms.web.dao.inv.create;

import com.hitech.dms.web.model.inv.create.request.InvoiceCreateRequestModel;
import com.hitech.dms.web.model.inv.create.response.InvoiceCreateResponseModel;

public interface InvoiceCreateDao {
	public InvoiceCreateResponseModel createInvoice(String authorizationHeader, String userCode,
			InvoiceCreateRequestModel requestModel);
}
