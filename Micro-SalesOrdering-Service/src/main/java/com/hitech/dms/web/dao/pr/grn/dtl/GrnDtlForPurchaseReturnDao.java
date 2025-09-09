/**
 * 
 */
package com.hitech.dms.web.dao.pr.grn.dtl;

import com.hitech.dms.web.model.pr.grn.dtl.request.GrnDtlForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.dtl.response.GrnDtlForPurchaseReturnResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface GrnDtlForPurchaseReturnDao {
	public GrnDtlForPurchaseReturnResponseModel fetchGrnDtlForPurchaseReturn(String userCode,
			GrnDtlForPurchaseReturnRequestModel requestModel);
}
