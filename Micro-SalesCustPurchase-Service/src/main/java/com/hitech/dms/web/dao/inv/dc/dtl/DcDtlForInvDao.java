package com.hitech.dms.web.dao.inv.dc.dtl;

import com.hitech.dms.web.model.inv.dc.dtl.request.DcDtlForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.dtl.response.DcDtlForInvResponseModel;

public interface DcDtlForInvDao {
	public DcDtlForInvResponseModel fetchDcDetailForInvoice(String userCode, DcDtlForInvRequestModel requestModel);
}
