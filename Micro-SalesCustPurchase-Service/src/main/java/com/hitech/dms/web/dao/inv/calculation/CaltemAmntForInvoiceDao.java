package com.hitech.dms.web.dao.inv.calculation;

import com.hitech.dms.web.model.inv.calculatAmnt.request.CaltemAmntForInvoiceRequestModel;
import com.hitech.dms.web.model.inv.calculatAmnt.response.CaltemAmntForInvoiceResponseModel;

public interface CaltemAmntForInvoiceDao {
	public CaltemAmntForInvoiceResponseModel calculateAmount(String userCode,
			CaltemAmntForInvoiceRequestModel requestModel);
}
