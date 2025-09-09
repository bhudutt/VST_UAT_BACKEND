package com.hitech.dms.web.dao.pr.inv.view;

import com.hitech.dms.web.model.pr.inv.view.request.PrForInvoiceViewRequestModel;
import com.hitech.dms.web.model.pr.inv.view.response.PrForInvoiceViewResponseModel;

public interface PrForInvoiceViewDao {
	public PrForInvoiceViewResponseModel fetchPurchaseReturnInvDtl(String userCode,
			PrForInvoiceViewRequestModel requestModel);
}
