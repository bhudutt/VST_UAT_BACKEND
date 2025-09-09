/**
 * 
 */
package com.hitech.dms.web.service.validator.invoice.create;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.inv.create.request.InvoiceCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class InvoiceCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return InvoiceCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking Invoice Create Validator form validator");
		}
		InvoiceCreateRequestModel requestModel = (InvoiceCreateRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}

		if (requestModel.getInvoiceTypeId() == null || requestModel.getInvoiceTypeId().compareTo(0) == 0) {
			errors.reject("invoiceTypeId", "Invoice Type is required.");
		}

		if (requestModel.getMachineInvoiceDtlList() == null || requestModel.getMachineInvoiceDtlList().isEmpty()) {
			errors.reject("machineInvoiceDtlList", "Please Select AtLeast One Machine.");
		}

		if (requestModel.getTotalBasicAmnt() == null
				|| requestModel.getTotalBasicAmnt().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalBasicAmnt", "Total Basic Amount is required.");
		}

		if (requestModel.getTotalGstAmnt() == null || requestModel.getTotalGstAmnt().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalGstAmnt", "Total Gst Amount is required.");
		}

		if (requestModel.getTotalInvoiceAmnt() == null
				|| requestModel.getTotalInvoiceAmnt().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalInvoiceAmnt", "Total Invoice Amount is required.");
		}
	}
}
