package com.hitech.dms.web.model.pcr;

import java.util.List;

import com.hitech.dms.web.entity.pcr.PartRequisitionDtlEntity;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcrDtlEntity;

import lombok.Data;

@Data
public class CreatePcrDto {
	ServiceWarrantyPcr serviceWarrantyPcr;
//    List<JobCardPcrPartDto> jobCardPcrPartDto;
//    List<PartRequisitionDtlEntity> partRequisitionDtlEntity;
    List<ServiceWarrantyPcrDtlEntity> serviceWarrantyPcrDtlEntity;

}
