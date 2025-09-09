/**
 * 
 */
package com.hitech.dms.web.service.validator.invoice.cancel;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.inv.cancel.request.InvCancelRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class InvoiceCancelValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceCancelValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return InvCancelRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking Invoice Validator form validator");
		}
		InvCancelRequestModel requestModel = (InvCancelRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}
		
		if (requestModel.getSalesInvoiceHdrId() == null || requestModel.getSalesInvoiceHdrId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("salesInvoiceHdrId", "Invoice Number is required.");
		}

		if (requestModel.getInvCancelDate() == null) {
			errors.reject("invCancelDate", "Invoice Cancel Date is required.");
		}

		if (requestModel.getInvCancelReasonId() == null
				|| requestModel.getInvCancelReasonId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("invCancelReasonId", "Invoice Cancel Reason is required.");
		}

		if (requestModel.getInvCancelRemark() == null || requestModel.getInvCancelRemark().equals("")) {
			errors.reject("invCancelRemark", "Invoice Cancel Remarks is required.");
		}
	}
}
