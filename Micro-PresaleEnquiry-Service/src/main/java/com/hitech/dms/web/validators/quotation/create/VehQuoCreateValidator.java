/**
 * 
 */
package com.hitech.dms.web.validators.quotation.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.quotation.create.request.VehQuoDTLRequestModel;
import com.hitech.dms.web.model.quotation.create.request.VehQuoHDRRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class VehQuoCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(VehQuoCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return VehQuoHDRRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		VehQuoHDRRequestModel hdrRequestModel = (VehQuoHDRRequestModel) target;
		if (hdrRequestModel.getBranchId() == null) {
			errors.reject("branchId", "BranchId is required.");
		}
		if (hdrRequestModel.getPcId() == null || hdrRequestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (hdrRequestModel.getCustomerId() == null
				|| hdrRequestModel.getCustomerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("customerId", "Customer is required.");
		}

		if (hdrRequestModel.getTotalAmount() == null
				|| hdrRequestModel.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
			errors.reject("totalAmount", "Total Amount is required.");
		}
		
		List<VehQuoDTLRequestModel> vehQuoDTLList = hdrRequestModel.getVehQuoDTLList();
		if(vehQuoDTLList == null || vehQuoDTLList.isEmpty()) {
			errors.reject("vehQuoDTLList", "Please Select At Least One Enquiry.");
		}else {
			for(int i = 0; i < vehQuoDTLList.size(); i ++) {
				VehQuoDTLRequestModel quoDTLModel  = vehQuoDTLList.get(i);
				if(quoDTLModel.getMachineItemId() == null || quoDTLModel.getMachineItemId().compareTo(BigInteger.ZERO) == 0) {
					errors.reject("vehQuoDTLList["+i+"].machineItemId", "Machine Item is required.");
				}
				if(quoDTLModel.getTotalItemAmnt() == null || quoDTLModel.getTotalItemAmnt().compareTo(BigDecimal.ZERO) == 0) {
					errors.reject("vehQuoDTLList["+i+"].totalItemAmnt", "Total Amount is required.");
				}
			}
		}
	}
}
