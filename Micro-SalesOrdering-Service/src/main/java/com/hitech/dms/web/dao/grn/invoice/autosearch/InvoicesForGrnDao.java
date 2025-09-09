package com.hitech.dms.web.dao.grn.invoice.autosearch;

import java.util.List;

import com.hitech.dms.web.model.grn.invoice.autosearch.request.InvoicesForGrnRequestModel;
import com.hitech.dms.web.model.grn.invoice.autosearch.response.InvoicesForGrnResponseModel;

public interface InvoicesForGrnDao {
	public List<InvoicesForGrnResponseModel> fetchInvoiceListForGrn(String userCode,
			InvoicesForGrnRequestModel requestModel);
}
