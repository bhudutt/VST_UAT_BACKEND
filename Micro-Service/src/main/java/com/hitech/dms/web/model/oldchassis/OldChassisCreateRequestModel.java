package com.hitech.dms.web.model.oldchassis;

import com.hitech.dms.web.entity.customer.ChassisMachineVinMstEntity;
import com.hitech.dms.web.entity.customer.CustomerDTLEntity;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.customer.CustomerItemDTLEntity;

import lombok.Data;

@Data
public class OldChassisCreateRequestModel {
	
	private ChassisMachineVinMstEntity chassisMachineVinMstEntity;
	private CustomerHDREntity customerHDREntity;
	private CustomerDTLEntity customerDTLEntity;
	private CustomerItemDTLEntity customerItemDTLEntity;
	
}
