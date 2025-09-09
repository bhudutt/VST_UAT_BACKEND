package com.hitech.dms.web.dao.pr.inv.dtl;

import com.hitech.dms.web.model.pr.inv.dtl.request.PrDtlFornvoiceRequestModel;
import com.hitech.dms.web.model.pr.inv.dtl.response.PrDtlFornvoiceResponseModel;

public interface PrDtlFornvoiceDao {
	public PrDtlFornvoiceResponseModel fetchPurchaseReturnDtlForInv(String userCode,
			PrDtlFornvoiceRequestModel requestModel);
}
