package com.hitech.dms.web.model.partrequisition.create.request;



import java.util.*;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.hitech.dms.web.entity.partrequisition.PartDetailsEntity;
import com.hitech.dms.web.entity.partrequisition.PartRequisitionEntity;

import lombok.Data;

@Data
public class SparePartRequisitionRequestModel {
 
	//private PartDetailsEntity partdetailsEntity;
	private PartRequisitionEntity partRequisitionEntity;
	private List<PartDetailsEntity> partdetailsEntity;	
	
	
}
