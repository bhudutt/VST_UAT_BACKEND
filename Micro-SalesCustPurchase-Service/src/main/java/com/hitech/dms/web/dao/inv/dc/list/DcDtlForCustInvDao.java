/**
 * 
 */
package com.hitech.dms.web.dao.inv.dc.list;

import com.hitech.dms.web.model.inv.dc.list.request.DcListForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcDtlForCustInvResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface DcDtlForCustInvDao {
	public DcDtlForCustInvResponseModel fetchInvoiceDCDtlForCustomer(String userCode,
			DcListForInvRequestModel requestModel);
}
