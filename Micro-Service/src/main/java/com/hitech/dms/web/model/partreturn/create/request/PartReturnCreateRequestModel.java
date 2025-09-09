package com.hitech.dms.web.model.partreturn.create.request;

import java.util.List;

import com.hitech.dms.web.entity.partrequisition.PartReturnDTLEntity;
import com.hitech.dms.web.entity.partrequisition.PartReturnHDREntity;

import lombok.Data;

@Data
public class PartReturnCreateRequestModel {

	private PartReturnHDREntity partReturnHDREntity;
	private List<PartReturnDTLEntity> partReturnDTLEntity;
}
