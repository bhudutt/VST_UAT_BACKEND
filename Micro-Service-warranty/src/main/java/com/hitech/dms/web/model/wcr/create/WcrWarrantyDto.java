package com.hitech.dms.web.model.wcr.create;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class WcrWarrantyDto {
//	PcrWarrantyClaimDto pcrWarrantyClaimDto;
//    GoodwillViewDto goodwillViewDto;
//    WarrantyWcrView warrantyWcrView;
    List<Map<String,Object>> labourCharge;
    List<Map<String,Object>> outSideLabourCharge;
    List<Map<String,Object>> warrantyPart;
    List<Map<String,Object>> approvalHierDetails;
    Boolean isShowInspectionBtn;
    Map<String,Boolean> invoiceType;

}
