package com.hitech.dms.web.entity.admin.org;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgHierarchyDto {
    
    private Long orgHierarchyId;
    private Long levelId;
    private String hierarchyCode;
    private String hierarchyDesc;
    private Long parentOrgHierarchyId;
    private String isActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdDate;
    private String createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private String modifiedBy;
    
    // Tree structure
    private List<OrgHierarchyDto> children = new ArrayList<>();
    
    // Level information
    private String levelDesc;
    private Integer levelSeqNo;
    
    // Constructors
    public OrgHierarchyDto() {}
    
    // Getters and Setters
    public Long getOrgHierarchyId() {
        return orgHierarchyId;
    }
    
    public void setOrgHierarchyId(Long orgHierarchyId) {
        this.orgHierarchyId = orgHierarchyId;
    }
    
    public Long getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
    
    public String getHierarchyCode() {
        return hierarchyCode;
    }
    
    public void setHierarchyCode(String hierarchyCode) {
        this.hierarchyCode = hierarchyCode;
    }
    
    public String getHierarchyDesc() {
        return hierarchyDesc;
    }
    
    public void setHierarchyDesc(String hierarchyDesc) {
        this.hierarchyDesc = hierarchyDesc;
    }
    
    public Long getParentOrgHierarchyId() {
        return parentOrgHierarchyId;
    }
    
    public void setParentOrgHierarchyId(Long parentOrgHierarchyId) {
        this.parentOrgHierarchyId = parentOrgHierarchyId;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public String getModifiedBy() {
        return modifiedBy;
    }
    
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public List<OrgHierarchyDto> getChildren() {
        return children;
    }
    
    public void setChildren(List<OrgHierarchyDto> children) {
        this.children = children;
    }
    
    public String getLevelDesc() {
        return levelDesc;
    }
    
    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }
    
    public Integer getLevelSeqNo() {
        return levelSeqNo;
    }
    
    public void setLevelSeqNo(Integer levelSeqNo) {
        this.levelSeqNo = levelSeqNo;
    }
}