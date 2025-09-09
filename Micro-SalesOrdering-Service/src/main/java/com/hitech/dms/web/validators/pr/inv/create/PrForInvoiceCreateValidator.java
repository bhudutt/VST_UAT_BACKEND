/**
 * 
 */
package com.hitech.dms.web.validators.pr.inv.create;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.pr.inv.create.request.PrForInvoiceCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class PrForInvoiceCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(PrForInvoiceCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return PrForInvoiceCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PrForInvoiceCreateRequestModel requestModel = (PrForInvoiceCreateRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getPurchaseReturnId() == null
				|| requestModel.getPurchaseReturnId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("purchaseReturnId", "Purchase Return Number is required.");
		}
		
		if ((requestModel.getSalesMachineGrnDtlList() == null || requestModel.getSalesMachineGrnDtlList().isEmpty())
				&& (requestModel.getSalesMachineGrnImplDtlList() == null
						|| requestModel.getSalesMachineGrnImplDtlList().isEmpty())) {
			errors.reject("salesMachineGrnDtlList", "At Least One Line Item Id required To Return.");
		}
	}
}
