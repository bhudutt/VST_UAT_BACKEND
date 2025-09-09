package com.hitech.dms.web.service.admin.org.add;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.web.entity.admin.org.BusinessValidationException;
import com.hitech.dms.web.entity.admin.org.DuplicateResourceException;
import com.hitech.dms.web.entity.admin.org.OrgHierarchy;
import com.hitech.dms.web.entity.admin.org.OrgHierarchyDto;
import com.hitech.dms.web.entity.admin.org.OrgLevel;
import com.hitech.dms.web.entity.admin.org.ResourceNotFoundException;
import com.hitech.dms.web.repo.admin.org.add.OrgHierarchyRepository;
import com.hitech.dms.web.repo.admin.org.add.OrgLevelRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrgHierarchyService {
    
	@Autowired
    private OrgHierarchyRepository orgHierarchyRepository;
    
    @Autowired
    private OrgLevelRepository orgLevelRepository;
    
    // ====================== ORIGINAL METHODS ======================
    
    public List<OrgHierarchy> getAllActiveHierarchies() {
        return orgHierarchyRepository.findByIsActiveOrderByOrgHierarchyId("Y");
    }
    
    public List<OrgHierarchy> getRootHierarchies() {
        return orgHierarchyRepository.findByParentOrgHierarchyIdIsNullAndIsActiveOrderByOrgHierarchyId("Y");
    }
    
    public List<OrgHierarchy> getChildrenByParentId(Long parentId) {
        return orgHierarchyRepository.findByParentOrgHierarchyIdAndIsActiveOrderByOrgHierarchyId(parentId, "Y");
    }
    
    public List<OrgHierarchy> getHierarchiesByLevel(Long levelId) {
        return orgHierarchyRepository.findByLevelIdAndIsActiveOrderByOrgHierarchyId(levelId, "Y");
    }
    
    public Optional<OrgHierarchy> getHierarchyById(Long hierarchyId) {
        return orgHierarchyRepository.findById(hierarchyId);
    }
    
    public OrgHierarchy getHierarchyByIdOrThrow(Long hierarchyId) {
        return orgHierarchyRepository.findById(hierarchyId)
            .orElseThrow(() -> new ResourceNotFoundException("OrgHierarchy not found with id: " + hierarchyId));
    }
    
    public Optional<OrgHierarchy> getHierarchyByCode(String hierarchyCode) {
        return orgHierarchyRepository.findByHierarchyCodeAndIsActive(hierarchyCode, "Y");
    }
    
    public List<OrgHierarchy> getCompleteHierarchyTree() {
        return orgHierarchyRepository.findAllWithRelationships("Y");
    }
    
    public List<OrgHierarchy> getRootHierarchiesWithChildren() {
        return orgHierarchyRepository.findRootHierarchiesWithChildren("Y");
    }
    
    public List<OrgHierarchy> searchHierarchies(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().length() < 2) {
            throw new BusinessValidationException("Search term must be at least 2 characters long");
        }
        return orgHierarchyRepository.searchByDescriptionOrCode(searchTerm.trim());
    }
    
    // ====================== NEW DTO METHODS ======================
    
    /**
     * Get complete hierarchy tree as DTOs with proper tree structure
     * This method resolves the lazy loading issue by building DTOs
     */
    public List<OrgHierarchyDto> getCompleteHierarchyTreeDto() {
        // Get all hierarchies
        List<OrgHierarchy> hierarchies = orgHierarchyRepository.findAllWithRelationships("Y");
        
        // Get all levels for reference
        List<OrgLevel> levels = orgLevelRepository.findByIsActiveOrderBySeqNo("Y");
        Map<Long, OrgLevel> levelMap = levels.stream()
            .collect(Collectors.toMap(OrgLevel::getLevelId, level -> level));
        
        return buildHierarchyTreeDto(hierarchies, levelMap);
    }
    
    /**
     * Convert single entity to DTO
     */
    public OrgHierarchyDto convertToDto(OrgHierarchy hierarchy) {
        List<OrgLevel> levels = orgLevelRepository.findByIsActiveOrderBySeqNo("Y");
        Map<Long, OrgLevel> levelMap = levels.stream()
            .collect(Collectors.toMap(OrgLevel::getLevelId, level -> level));
        
        return convertEntityToDto(hierarchy, levelMap);
    }
    
    // ====================== PRIVATE DTO HELPER METHODS ======================
    
    private List<OrgHierarchyDto> buildHierarchyTreeDto(List<OrgHierarchy> hierarchies, Map<Long, OrgLevel> levelMap) {
        // Convert entities to DTOs
        Map<Long, OrgHierarchyDto> dtoMap = new HashMap<>();
        
        for (OrgHierarchy hierarchy : hierarchies) {
            OrgHierarchyDto dto = convertEntityToDto(hierarchy, levelMap);
            dtoMap.put(hierarchy.getOrgHierarchyId(), dto);
        }
        
        // Build tree structure
        List<OrgHierarchyDto> rootNodes = new ArrayList<>();
        
        for (OrgHierarchyDto dto : dtoMap.values()) {
            if (dto.getParentOrgHierarchyId() == null) {
                // This is a root node
                rootNodes.add(dto);
            } else {
                // This is a child node, add it to its parent
                OrgHierarchyDto parent = dtoMap.get(dto.getParentOrgHierarchyId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }
        
        return rootNodes;
    }
    
    private OrgHierarchyDto convertEntityToDto(OrgHierarchy hierarchy, Map<Long, OrgLevel> levelMap) {
        OrgHierarchyDto dto = new OrgHierarchyDto();
        
        // Copy basic fields
        dto.setOrgHierarchyId(hierarchy.getOrgHierarchyId());
        dto.setLevelId(hierarchy.getLevelId());
        dto.setHierarchyCode(hierarchy.getHierarchyCode());
        dto.setHierarchyDesc(hierarchy.getHierarchyDesc());
        dto.setParentOrgHierarchyId(hierarchy.getParentOrgHierarchyId());
        dto.setIsActive(hierarchy.getIsActive());
        dto.setCreatedDate(hierarchy.getCreatedDate());
        dto.setCreatedBy(hierarchy.getCreatedBy());
        dto.setModifiedDate(hierarchy.getModifiedDate());
        dto.setModifiedBy(hierarchy.getModifiedBy());
        
        // Add level information if available
        OrgLevel level = levelMap.get(hierarchy.getLevelId());
        if (level != null) {
            dto.setLevelDesc(level.getLevelDesc());
            dto.setLevelSeqNo(level.getSeqNo());
        }
        
        return dto;
    }
    
    // ====================== ORIGINAL CRUD METHODS ======================
    
    public OrgHierarchy createHierarchy(OrgHierarchy hierarchy) {
        validateHierarchy(hierarchy, false);
        
        if (orgHierarchyRepository.existsByHierarchyCode(hierarchy.getHierarchyCode())) {
            throw new DuplicateResourceException("Hierarchy code already exists: " + hierarchy.getHierarchyCode());
        }
        
        OrgLevel orgLevel = getLevelByIdOrThrow(hierarchy.getLevelId());
        
        if (hierarchy.getParentOrgHierarchyId() != null) {
            OrgHierarchy parentHierarchy = getHierarchyByIdOrThrow(hierarchy.getParentOrgHierarchyId());
            if (!"Y".equals(parentHierarchy.getIsActive())) {
                throw new BusinessValidationException("Parent hierarchy is not active");
            }
        }
        
        hierarchy.setCreatedDate(LocalDateTime.now());
        if (hierarchy.getIsActive() == null) {
            hierarchy.setIsActive("Y");
        }
        
        return orgHierarchyRepository.save(hierarchy);
    }
    
    public OrgHierarchy updateHierarchy(Long hierarchyId, OrgHierarchy updatedHierarchy) {
        OrgHierarchy existing = getHierarchyByIdOrThrow(hierarchyId);
        
        validateHierarchy(updatedHierarchy, true);
        
        if (!existing.getHierarchyCode().equals(updatedHierarchy.getHierarchyCode()) &&
            orgHierarchyRepository.existsByHierarchyCodeAndOrgHierarchyIdNot(updatedHierarchy.getHierarchyCode(), hierarchyId)) {
            throw new DuplicateResourceException("Hierarchy code already exists: " + updatedHierarchy.getHierarchyCode());
        }
        
        OrgLevel orgLevel = getLevelByIdOrThrow(updatedHierarchy.getLevelId());
        
        if (updatedHierarchy.getParentOrgHierarchyId() != null) {
            if (updatedHierarchy.getParentOrgHierarchyId().equals(hierarchyId)) {
                throw new BusinessValidationException("Cannot set parent to self");
            }
            
            Long circularCount = orgHierarchyRepository.checkCircularReference(
                updatedHierarchy.getParentOrgHierarchyId(), hierarchyId);
            if (circularCount > 0) {
                throw new BusinessValidationException("Setting this parent would create a circular reference");
            }
            
            OrgHierarchy parentHierarchy = getHierarchyByIdOrThrow(updatedHierarchy.getParentOrgHierarchyId());
            if (!"Y".equals(parentHierarchy.getIsActive())) {
                throw new BusinessValidationException("Parent hierarchy is not active");
            }
        }
        
        // Update fields
        existing.setLevelId(updatedHierarchy.getLevelId());
        existing.setHierarchyCode(updatedHierarchy.getHierarchyCode());
        existing.setHierarchyDesc(updatedHierarchy.getHierarchyDesc());
        existing.setParentOrgHierarchyId(updatedHierarchy.getParentOrgHierarchyId());
        existing.setIsActive(updatedHierarchy.getIsActive());
        existing.setModifiedDate(LocalDateTime.now());
        existing.setModifiedBy(updatedHierarchy.getModifiedBy());
        
        return orgHierarchyRepository.save(existing);
    }
    
   
    
    public void deleteHierarchyWithChildren(Long hierarchyId, String modifiedBy) {
        getHierarchyByIdOrThrow(hierarchyId);
        
        int updatedCount = orgHierarchyRepository.softDeleteHierarchyAndChildren(hierarchyId, modifiedBy);
        if (updatedCount == 0) {
            throw new ResourceNotFoundException("OrgHierarchy not found with id: " + hierarchyId);
        }
    }
    
    public List<Object[]> getHierarchyPath(Long hierarchyId) {
        getHierarchyByIdOrThrow(hierarchyId);
        return orgHierarchyRepository.getHierarchyPath(hierarchyId);
    }
    
    public List<Object[]> getHierarchyDescendants(Long hierarchyId) {
        getHierarchyByIdOrThrow(hierarchyId);
        return orgHierarchyRepository.getHierarchyDescendants(hierarchyId);
    }
    
    public Long getChildrenCount(Long parentId) {
        return orgHierarchyRepository.countDirectChildren(parentId);
    }
    
    public boolean existsByHierarchyCode(String hierarchyCode) {
        return orgHierarchyRepository.existsByHierarchyCode(hierarchyCode);
    }
    
    // ====================== HELPER METHODS ======================
    
    private OrgLevel getLevelByIdOrThrow(Long levelId) {
        return orgLevelRepository.findById(levelId)
            .orElseThrow(() -> new ResourceNotFoundException("OrgLevel not found with id: " + levelId));
    }
    
    private void validateHierarchy(OrgHierarchy hierarchy, boolean isUpdate) {
        if (hierarchy == null) {
            throw new BusinessValidationException("Hierarchy cannot be null");
        }
        
        if (hierarchy.getLevelId() == null) {
            throw new BusinessValidationException("Level ID is required");
        }
        
        if (hierarchy.getHierarchyCode() == null || hierarchy.getHierarchyCode().trim().isEmpty()) {
            throw new BusinessValidationException("Hierarchy code is required");
        }
        
        if (hierarchy.getHierarchyDesc() == null || hierarchy.getHierarchyDesc().trim().isEmpty()) {
            throw new BusinessValidationException("Hierarchy description is required");
        }
        
        if (!isUpdate && (hierarchy.getCreatedBy() == null || hierarchy.getCreatedBy().trim().isEmpty())) {
            throw new BusinessValidationException("Created by is required");
        }
    }
}