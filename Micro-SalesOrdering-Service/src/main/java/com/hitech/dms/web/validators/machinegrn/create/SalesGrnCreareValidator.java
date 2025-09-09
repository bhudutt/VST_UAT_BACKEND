/**
 * 
 */
package com.hitech.dms.web.validators.machinegrn.create;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.grn.create.request.SalesGrnCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class SalesGrnCreareValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(SalesGrnCreareValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return SalesGrnCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SalesGrnCreateRequestModel requestModel = (SalesGrnCreateRequestModel) target;
		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getGrnTypeId() == null || requestModel.getGrnTypeId().compareTo(0) == 0) {
			errors.reject("grnTypeId", "Grn Type is required.");
		}
		
		if (requestModel.getInvoiceNumber() == null || requestModel.getInvoiceNumber().equals("")) {
			errors.reject("invoiceNumber", "Invoice Number is required.");
		}
		
		if (requestModel.getTransporterName() == null || requestModel.getTransporterName().equals("")) {
			errors.reject("transporterName", "Transporter Name is required.");
		}
		
		if (requestModel.getTransporterVehicleNo() == null || requestModel.getTransporterVehicleNo().equals("")) {
			errors.reject("transporterVehicleNo", "Transporter Vehicle is required.");
		}
		
		if ((requestModel.getSalesMachineGrnDtlList() == null || requestModel.getSalesMachineGrnDtlList().isEmpty())
				&& (requestModel.getSalesMachineGrnImplDtlList() == null || requestModel.getSalesMachineGrnImplDtlList().isEmpty())) {
			errors.reject("SalesMachineGrnDtlList", "AtLeast One Machine/Implement/Accessory Detail is required.");
		}
	}
}
