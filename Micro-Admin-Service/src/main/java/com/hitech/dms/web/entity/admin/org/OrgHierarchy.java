package com.hitech.dms.web.entity.admin.org;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ADM_HO_MST_ORG_LEVEL_HIER")
public class OrgHierarchy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_hierarchy_id")
    private Long orgHierarchyId;
    
    @NotNull(message = "Level ID is required")
    @Min(value = 1, message = "Level ID must be greater than 0")
    @Column(name = "level_id", nullable = false)
    private Long levelId;
    
    @NotBlank(message = "Hierarchy code is required")
    @Size(max = 50, message = "Hierarchy code cannot exceed 50 characters")
    //@Pattern(regexp = "^[A-Z0-9\\-_]+$", message = "Hierarchy code can only contain uppercase letters, numbers, hyphens, and underscores")
    @Column(name = "hierarchy_code", nullable = false, length = 50, unique = true)
    private String hierarchyCode;
    
    @NotBlank(message = "Hierarchy description is required")
    @Size(max = 200, message = "Hierarchy description cannot exceed 200 characters")
    @Column(name = "hierarchy_desc", nullable = false, length = 200)
    private String hierarchyDesc;
    
    @Column(name = "parent_org_hierarchy_id")
    private Long parentOrgHierarchyId;
    
    @NotBlank(message = "Active status is required")
    //@Pattern(regexp = "^[YN]$", message = "Active status must be Y or N")
    @Column(name = "isActive", nullable = false, length = 1)
    private String isActive;
    
    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @NotBlank(message = "Created by is required")
    @Size(max = 50, message = "Created by cannot exceed 50 characters")
    @Column(name = "CreatedBy", nullable = false, length = 50, updatable = false)
    private String createdBy;
    
    @Column(name = "ModifiedDate")
    private LocalDateTime modifiedDate;
    
    @Size(max = 50, message = "Modified by cannot exceed 50 characters")
    @Column(name = "ModifiedBy", length = 50)
    private String modifiedBy;
    
    // Many-to-One relationship with OrgLevel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", insertable = false, updatable = false)
    @JsonBackReference
    private OrgLevel orgLevel;
    
    // Self-referencing relationship for hierarchy (Parent)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_org_hierarchy_id", insertable = false, updatable = false)
    @JsonBackReference("parent-child")
    private OrgHierarchy parent;
    
    // Self-referencing relationship for hierarchy (Children)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("parent-child")
    private List<OrgHierarchy> children = new ArrayList<>();
    
    // Constructors
    public OrgHierarchy() {
        this.isActive = "Y";
        this.createdDate = LocalDateTime.now();
    }
    
    public OrgHierarchy(Long levelId, String hierarchyCode, String hierarchyDesc, 
                       Long parentOrgHierarchyId, String createdBy) {
        this();
        this.levelId = levelId;
        this.hierarchyCode = hierarchyCode;
        this.hierarchyDesc = hierarchyDesc;
        this.parentOrgHierarchyId = parentOrgHierarchyId;
        this.createdBy = createdBy;
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = "Y";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
    
    // Business methods
    public void addChild(OrgHierarchy child) {
        children.add(child);
        child.setParent(this);
        child.setParentOrgHierarchyId(this.orgHierarchyId);
    }
    
    public void removeChild(OrgHierarchy child) {
        children.remove(child);
        child.setParent(null);
        child.setParentOrgHierarchyId(null);
    }
    
    public boolean isRoot() {
        return parentOrgHierarchyId == null;
    }
    
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
    
    public int getLevel() {
        if (isRoot()) return 0;
        if (parent != null) return parent.getLevel() + 1;
        return 0; // fallback
    }
    
    // Getters and Setters
    public Long getOrgHierarchyId() { return orgHierarchyId; }
    public void setOrgHierarchyId(Long orgHierarchyId) { this.orgHierarchyId = orgHierarchyId; }
    
    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    
    public String getHierarchyCode() { return hierarchyCode; }
    public void setHierarchyCode(String hierarchyCode) { this.hierarchyCode = hierarchyCode; }
    
    public String getHierarchyDesc() { return hierarchyDesc; }
    public void setHierarchyDesc(String hierarchyDesc) { this.hierarchyDesc = hierarchyDesc; }
    
    public Long getParentOrgHierarchyId() { return parentOrgHierarchyId; }
    public void setParentOrgHierarchyId(Long parentOrgHierarchyId) { 
        this.parentOrgHierarchyId = parentOrgHierarchyId; 
    }
    
    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
    
    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
    
    public OrgLevel getOrgLevel() { return orgLevel; }
    public void setOrgLevel(OrgLevel orgLevel) { this.orgLevel = orgLevel; }
    
    public OrgHierarchy getParent() { return parent; }
    public void setParent(OrgHierarchy parent) { this.parent = parent; }
    
    public List<OrgHierarchy> getChildren() { return children; }
    public void setChildren(List<OrgHierarchy> children) { this.children = children; }
    
    @Override
    public String toString() {
        return "OrgHierarchy{" +
               "orgHierarchyId=" + orgHierarchyId +
               ", hierarchyCode='" + hierarchyCode + '\'' +
               ", hierarchyDesc='" + hierarchyDesc + '\'' +
               ", parentOrgHierarchyId=" + parentOrgHierarchyId +
               ", levelId=" + levelId +
               ", isActive='" + isActive + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrgHierarchy)) return false;
        OrgHierarchy that = (OrgHierarchy) o;
        return orgHierarchyId != null && orgHierarchyId.equals(that.orgHierarchyId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
