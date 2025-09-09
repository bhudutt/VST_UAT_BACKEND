package com.hitech.dms.web.service.admin.org.add;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.web.entity.admin.org.DuplicateResourceException;
import com.hitech.dms.web.entity.admin.org.OrgLevel;
import com.hitech.dms.web.entity.admin.org.OrgLevelDTO;
import com.hitech.dms.web.entity.admin.org.ResourceNotFoundException;
import com.hitech.dms.web.repo.admin.org.add.OrgLevelRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrgLevelService {
    
	@Autowired
    private OrgLevelRepository orgLevelRepository;
	
	public List<OrgLevelDTO> getAllLevelsDTO() {
        List<OrgLevel> levels = orgLevelRepository.findAll();
        return levels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
	
	private OrgLevelDTO convertToDTO(OrgLevel level) {
        return OrgLevelDTO.builder()
                .levelId(level.getLevelId())
                .pcId(level.getPcId())
                .departmentId(level.getDepartmentId())
                .levelCode(level.getLevelCode())
                .levelDesc(level.getLevelDesc())
                .seqNo(level.getSeqNo())
                .isActive(level.getIsActive())
                .createdDate(level.getCreatedDate())
                .createdBy(level.getCreatedBy())
                .modifiedDate(level.getModifiedDate())
                .modifiedBy(level.getModifiedBy())
                // Don't include hierarchies unless specifically needed
                .build();
    }
    
    public List<OrgLevel> getAllActiveLevels() {
        return orgLevelRepository.findByIsActiveOrderBySeqNo("Y");
    }
    
    public List<OrgLevel> getLevelsByPcAndDepartment(Long pcId, Long departmentId) {
        return orgLevelRepository.findActiveLevelsByPcAndDepartment(pcId, departmentId);
    }
    
    public Optional<OrgLevel> getLevelById(Long levelId) {
        return orgLevelRepository.findById(levelId);
    }
    
    public OrgLevel getLevelByIdOrThrow(Long levelId) {
        return orgLevelRepository.findById(levelId)
            .orElseThrow(() -> new ResourceNotFoundException("OrgLevel not found with id: " + levelId));
    }
    
    public Optional<OrgLevel> getLevelByCode(String levelCode) {
        return orgLevelRepository.findByLevelCodeAndIsActive(levelCode, "Y");
    }
    
    public List<OrgLevel> searchLevelsByDescription(String description) {
        return orgLevelRepository.findByLevelDescContainingIgnoreCaseAndIsActive(description, "Y");
    }
    
    public OrgLevel createLevel(OrgLevel orgLevel) {
        // Validate unique level code
        if (orgLevelRepository.existsByLevelCode(orgLevel.getLevelCode())) {
            throw new DuplicateResourceException("Level code already exists: " + orgLevel.getLevelCode());
        }
        
        // Set sequence number if not provided
        if (orgLevel.getSeqNo() == null) {
            Integer nextSeq = orgLevelRepository.getNextSequenceNumber(orgLevel.getPcId(), orgLevel.getDepartmentId());
            orgLevel.setSeqNo(nextSeq);
        }
        
        // Set audit fields
        orgLevel.setCreatedDate(LocalDateTime.now());
        if (orgLevel.getIsActive() == null) {
            orgLevel.setIsActive("Y");
        }
        
        return orgLevelRepository.save(orgLevel);
    }
    
    public OrgLevel updateLevel(Long levelId, OrgLevel updatedLevel) {
        OrgLevel existing = getLevelByIdOrThrow(levelId);
        
        // Validate unique level code (excluding current record)
        if (!existing.getLevelCode().equals(updatedLevel.getLevelCode()) &&
            orgLevelRepository.existsByLevelCodeAndLevelIdNot(updatedLevel.getLevelCode(), levelId)) {
            throw new DuplicateResourceException("Level code already exists: " + updatedLevel.getLevelCode());
        }
        
        // Update fields
        existing.setPcId(updatedLevel.getPcId());
        existing.setDepartmentId(updatedLevel.getDepartmentId());
        existing.setLevelCode(updatedLevel.getLevelCode());
        existing.setLevelDesc(updatedLevel.getLevelDesc());
        existing.setSeqNo(updatedLevel.getSeqNo());
        existing.setIsActive(updatedLevel.getIsActive());
        existing.setModifiedDate(LocalDateTime.now());
        existing.setModifiedBy(updatedLevel.getModifiedBy());
        
        return orgLevelRepository.save(existing);
    }
    
    public void deleteLevel(Long levelId, String modifiedBy) {
        OrgLevel level = getLevelByIdOrThrow(levelId);
        
        // Check if level has active hierarchies
        List<Object[]> levelsWithCount = orgLevelRepository.findLevelsWithHierarchyCount("Y");
        long hierarchyCount = levelsWithCount.stream()
            .filter(arr -> ((OrgLevel) arr[0]).getLevelId().equals(levelId))
            .mapToLong(arr -> (Long) arr[1])
            .findFirst()
            .orElse(0L);
        
        if (hierarchyCount > 0) {
            throw new IllegalStateException("Cannot delete level with active hierarchies. Found " + hierarchyCount + " active hierarchies.");
        }
        
        // Soft delete
        level.setIsActive("N");
        level.setModifiedDate(LocalDateTime.now());
        level.setModifiedBy(modifiedBy);
        orgLevelRepository.save(level);
    }
    
    public List<Object[]> getLevelsWithHierarchyCount() {
        return orgLevelRepository.findLevelsWithHierarchyCount("Y");
    }
    
    public boolean existsByLevelCode(String levelCode) {
        return orgLevelRepository.existsByLevelCode(levelCode);
    }
}