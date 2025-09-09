package com.hitech.dms.web.model.pcr;

import java.util.List;
import lombok.Data;

@Data
public class JobCardPcrDto {
//	ServiceWarrantyPcr serviceWarrantyPcr;
	JobCardPcrViewDto jobCardPcrViewDto;
	List<CustomerVoiceDto> customerVoiceDto;
	List<ServiceHistoryDto> serviceHistoryDto;
    List<JobCardPcrPartDto> JobCardPcrPartDto;
    List<LabourChargeDTO> labourCharge;
    List<LabourChargeDTO> outSideLabourCharge;
//    Map<String,Object> warrantyPeriod;

}
