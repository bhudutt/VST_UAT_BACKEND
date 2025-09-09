package com.hitech.dms.web.dao.pr.inv.create;

import com.hitech.dms.web.model.pr.inv.create.request.PrForInvoiceCreateRequestModel;
import com.hitech.dms.web.model.pr.inv.create.response.PrFornvoiceCreateResponseModel;

public interface PrForInvoiceCreateDao {
	public PrFornvoiceCreateResponseModel createMachinePurchaseReturnInv(String authorizationHeader, String userCode,
			PrForInvoiceCreateRequestModel requestModel);
}
