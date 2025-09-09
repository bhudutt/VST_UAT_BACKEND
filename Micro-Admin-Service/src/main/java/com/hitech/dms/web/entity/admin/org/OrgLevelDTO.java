package com.hitech.dms.web.entity.admin.org;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgLevelDTO {
    private Long levelId;
    private Long pcId;
    private Long departmentId;
    private String levelCode;
    private String levelDesc;
    private Integer seqNo;
    private String isActive;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
    
    // Only include hierarchies if explicitly needed
    private List<OrgHierarchyDto> hierarchies;
}