package com.hitech.dms.web.model.updateoldchassis;

import com.hitech.dms.web.entity.customer.CustomerDTLEntity;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.customer.CustomerItemDTLEntity;
import com.hitech.dms.web.entity.customer.VinMasterEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UpdateOldChassisRequest {

	private VinMasterEntity vinMasterEntity;
	private CustomerHDREntity customerHDREntity;
	private CustomerDTLEntity customerDTLEntity;
	private CustomerItemDTLEntity customerItemDTLEntity;
}
