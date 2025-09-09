package com.hitech.dms.web.entity.admin.org;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ADM_HO_MST_ORG_LEVEL")
public class OrgLevel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long levelId;
    
    @NotNull(message = "PC ID is required")
    @Min(value = 1, message = "PC ID must be greater than 0")
    @Column(name = "pc_id", nullable = false)
    private Long pcId;
    
    @NotNull(message = "Department ID is required")
    @Min(value = 1, message = "Department ID must be greater than 0")
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    
    @NotBlank(message = "Level code is required")
    @Size(max = 50, message = "Level code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9\\-_]+$", message = "Level code can only contain uppercase letters, numbers, hyphens, and underscores")
    @Column(name = "level_code", nullable = false, length = 50, unique = true)
    private String levelCode;
    
    @NotBlank(message = "Level description is required")
    @Size(max = 100, message = "Level description cannot exceed 100 characters")
    @Column(name = "level_desc", nullable = false, length = 100)
    private String levelDesc;
    
    @NotNull(message = "Sequence number is required")
    @Min(value = 1, message = "Sequence number must be greater than 0")
    @Max(value = 999, message = "Sequence number cannot exceed 999")
    @Column(name = "seq_no", nullable = false)
    private Integer seqNo;
    
    @NotBlank(message = "Active status is required")
    @Pattern(regexp = "^[YN]$", message = "Active status must be Y or N")
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
    
    // One-to-Many relationship with OrgHierarchy
    @OneToMany(mappedBy = "orgLevel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // @JsonManagedReference
    @JsonIgnore
    private List<OrgHierarchy> hierarchies;
    
    // Constructors
    public OrgLevel() {
        this.isActive = "Y";
        this.createdDate = LocalDateTime.now();
    }
    
    public OrgLevel(Long pcId, Long departmentId, String levelCode, String levelDesc, 
                   Integer seqNo, String createdBy) {
        this();
        this.pcId = pcId;
        this.departmentId = departmentId;
        this.levelCode = levelCode;
        this.levelDesc = levelDesc;
        this.seqNo = seqNo;
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
    
    // Getters and Setters
    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    
    public Long getPcId() { return pcId; }
    public void setPcId(Long pcId) { this.pcId = pcId; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getLevelCode() { return levelCode; }
    public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
    
    public String getLevelDesc() { return levelDesc; }
    public void setLevelDesc(String levelDesc) { this.levelDesc = levelDesc; }
    
    public Integer getSeqNo() { return seqNo; }
    public void setSeqNo(Integer seqNo) { this.seqNo = seqNo; }
    
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
    
    public List<OrgHierarchy> getHierarchies() { return hierarchies; }
    public void setHierarchies(List<OrgHierarchy> hierarchies) { this.hierarchies = hierarchies; }
    
    @Override
    public String toString() {
        return "OrgLevel{" +
               "levelId=" + levelId +
               ", levelCode='" + levelCode + '\'' +
               ", levelDesc='" + levelDesc + '\'' +
               ", seqNo=" + seqNo +
               ", isActive='" + isActive + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrgLevel)) return false;
        OrgLevel orgLevel = (OrgLevel) o;
        return levelId != null && levelId.equals(orgLevel.levelId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
