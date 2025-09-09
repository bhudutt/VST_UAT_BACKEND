/**
 * 
 */
package com.hitech.dms.web.dao.grn.invoice.dtl;

import com.hitech.dms.web.model.grn.invoice.dtl.request.InvoicesForGrnDtlRequestModel;
import com.hitech.dms.web.model.grn.invoice.dtl.response.InvoicesForGrnDtlResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface InvoicesForGrnDtlDao {
	public InvoicesForGrnDtlResponseModel fetchInvoiceDtlForGrn(String userCode,
			InvoicesForGrnDtlRequestModel requestModel);
}
