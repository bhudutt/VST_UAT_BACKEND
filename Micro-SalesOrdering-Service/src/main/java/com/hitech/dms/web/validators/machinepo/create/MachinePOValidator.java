/**
 * 
 */
package com.hitech.dms.web.validators.machinepo.create;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.machinepo.create.request.MachinePOCreateDtlRequestModel;
import com.hitech.dms.web.model.machinepo.create.request.MachinePOCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class MachinePOValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return MachinePOCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		MachinePOCreateRequestModel requestModel = (MachinePOCreateRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}
		if (requestModel.getPoTypeId() == null || requestModel.getPoTypeId().compareTo(0) == 0) {
			errors.reject("poTypeId", "PO ON is required.");
		}
		/*
		 * if (requestModel.getProductDivisionId() == null ||
		 * requestModel.getProductDivisionId().compareTo(0) == 0) {
		 * errors.reject("productDivisionId", "Product Division is required."); }
		 */

		if (requestModel.getBasicAmount() == null || requestModel.getBasicAmount().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("basicAmount", "Total Basic Amount is required.");
		}
		if (requestModel.getTotalGstAmount() == null
				|| requestModel.getTotalGstAmount().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalGstAmount", "Total Gst Amount is required.");
		}
		if (requestModel.getTotalAmount() == null || requestModel.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalAmount", "Total Amount is required.");
		}
		if (requestModel.getMachinePODtlList() == null || requestModel.getMachinePODtlList().isEmpty()) {
			errors.reject("machinePODtlList", "At Least One Machine Item is required.");
		}

	}
}
